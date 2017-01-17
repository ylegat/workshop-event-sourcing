package com.github.ylegat.workshop.domain.account;

import static com.github.ylegat.workshop.domain.account.BankAccount.loadBankAccount;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.ylegat.workshop.domain.common.EventStore;
import com.github.ylegat.workshop.domain.common.ProcessManager;

public class TransferProcessManager implements ProcessManager {

    private static final Logger logger = LoggerFactory.getLogger(TransferProcessManager.class);

    private final EventStore eventStore;

    public TransferProcessManager(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void on(BankAccountRegistered bankAccountRegistered) {

    }

    @Override
    public void on(CreditProvisioned creditProvisioned) {

    }

    @Override
    public void on(CreditWithdrawn creditWithdrawn) {

    }

    @Override
    public void on(TransferRequested transferRequested) {
        Optional<BankAccount> bankAccountDestination = loadBankAccount(transferRequested.bankAccountIdDestination, eventStore);

        if (bankAccountDestination.isPresent()) {
            executeCommand(() -> bankAccountDestination.get().receiveTransfer(transferRequested.aggregateId,
                                                                              transferRequested.transferId,
                                                                              transferRequested.creditTransferred));
        } else {
            Optional<BankAccount> bankAccountOrigin = loadBankAccount(transferRequested.aggregateId, eventStore);
            if (bankAccountOrigin.isPresent()) {
                executeCommand(() -> bankAccountOrigin.get().cancelTransfer(transferRequested.transferId));
            } else {
                logger.error("unknown account cannot cancel transfer {}", transferRequested);
            }
        }
    }

    @Override
    public void on(TransferReceived transferReceived) {
        Optional<BankAccount> bankAccountDestination = loadBankAccount(transferReceived.bankAccountIdOrigin, eventStore);
        if (bankAccountDestination.isPresent()) {
            executeCommand(() -> bankAccountDestination.get().completeTransfer(transferReceived.transferId));
        } else {
            logger.error("unknown account cannot complete transfer {}", transferReceived);
        }
    }

    private void executeCommand(Runnable command) {
        try {
            command.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void on(TransferCompleted transferCompleted) {

    }

    @Override
    public void on(TransferCanceled transferCanceled) {

    }
}
