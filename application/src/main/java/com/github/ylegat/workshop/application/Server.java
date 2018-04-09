package com.github.ylegat.workshop.application;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;
import static spark.Spark.post;
import com.github.ylegat.workshop.domain.account.BankAccount;
import com.github.ylegat.workshop.domain.account.TransferProcessManager;
import com.github.ylegat.workshop.domain.common.ConflictingEventException;
import com.github.ylegat.workshop.domain.common.EventStore;
import com.github.ylegat.workshop.domain.common.InvalidCommandException;
import com.github.ylegat.workshop.infrastructure.InMemoryEventBus;
import com.github.ylegat.workshop.infrastructure.InMemoryEventStore;
import com.github.ylegat.workshop.projection.balance.BalanceProjectionManager;
import com.github.ylegat.workshop.projection.balance.BalanceRepository;
import com.github.ylegat.workshop.projection.balance.InMemoryBalanceRepository;

public class Server {

    public static void main(String[] args) {
        new Server().run();
    }

    private final EventStore eventStore;

    private final BalanceRepository balanceRepository;

    public Server() {
        balanceRepository = new InMemoryBalanceRepository();
        balanceRepository.clear();

        InMemoryEventBus eventBus = new InMemoryEventBus();

        eventStore = new InMemoryEventStore(eventBus);
        eventStore.clear();

        eventBus.register(new TransferProcessManager(eventStore));
        eventBus.register(new BalanceProjectionManager(balanceRepository));

        /*
        http POST localhost:8080/account/account_1
        http POST localhost:8080/account/account_1/provision/100
        http POST localhost:8080/account/account_1/withdraw/500

        http GET localhost:8080/account/account_1/balance

        http POST localhost:8080/account/account_2
        http POST localhost:8080/account/account_2/provision/100
        http POST localhost:8080/account/account_2/withdraw/40

        http GET localhost:8080/account/account_2/balance

        http POST localhost:8080/account/account_1/transfer/5/to/account_2

        http GET localhost:8080/account/account_1/balance
        http GET localhost:8080/account/account_2/balance
         */
    }

    public void run() {
        port(8080);

        post("/account/:bankAccountId", (request, response) -> {
            String bankAccountId = request.params(":bankAccountId");

            handleError(() -> registerBankAccount(bankAccountId, eventStore));

            return format("bank account '%s' created", bankAccountId);
        });

        get("/account/:bankAccountId/balance", ((request, response) -> {
            String bankAccountId = request.params(":bankAccountId");

            return balanceRepository.readBalance(bankAccountId)
                                    .map(credit -> bankAccountId + ", balance: " + credit)
                                    .orElseGet(() -> {
                                            response.status(404);
                                            return format("bank account '%s' unknown", bankAccountId);
                                        });
        }));

        post("/account/:bankAccountId/provision/:credit", (request, response) -> {
            String bankAccountId = request.params(":bankAccountId");
            int creditOperation = parseInt(request.params(":credit"));

            handleError(() -> bankAccount(bankAccountId).provisionCredit(creditOperation));

            response.status(200);
            return format("provision +%s for account %s", creditOperation, bankAccountId);
        });

        post("/account/:bankAccountId/withdraw/:credit", (request, response) -> {
            String bankAccountId = request.params(":bankAccountId");
            int creditOperation = parseInt(request.params(":credit"));

            handleError(() -> bankAccount(bankAccountId).withdrawCredit(creditOperation));

            response.status(200);
            return format("withdraw -%s for account %s", creditOperation, bankAccountId);
        });

        post("/account/:bankAccountId/transfer/:credit/to/:bankAccountDestinationId",  ((request, response) -> {
            String bankAccountId = request.params(":bankAccountId");
            int creditOperation = parseInt(request.params(":credit"));
            String bankAccountDestinationId = request.params(":bankAccountDestinationId");

            handleError(() -> bankAccount(bankAccountId).requestTransfer(bankAccountDestinationId, creditOperation));

            response.status(200);
            return format("transfer of %s from %s to %s pending",
                          creditOperation,
                          bankAccountId,
                          bankAccountDestinationId);
        }));
    }

    private BankAccount bankAccount(String bankAccountId) {
        return BankAccount.loadBankAccount(bankAccountId, eventStore)
                          .orElseThrow(() -> halt(404, "bank account unknown"));
    }

    private void handleError(Runnable command) {
        try {
            command.run();

        } catch (InvalidCommandException e) {
            throw halt(400, "command invalid");
        } catch (ConflictingEventException e) {
            throw halt(409, "Conflict");
        }
    }
}
