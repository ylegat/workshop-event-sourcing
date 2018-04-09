package com.github.ylegat.workshop.domain.account;

import com.github.ylegat.workshop.domain.common.DecisionFunction;
import com.github.ylegat.workshop.domain.common.Event;
import com.github.ylegat.workshop.domain.common.EventListener;
import com.github.ylegat.workshop.domain.common.EventStore;
import com.github.ylegat.workshop.domain.common.EvolutionFunction;
import com.github.ylegat.workshop.domain.common.InvalidCommandException;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.UUID.randomUUID;

public class BankAccount {

    public static BankAccount registerBankAccount(String bankAccountId, EventStore eventStore) {
        BankAccount bankAccount = new BankAccount(eventStore);
        bankAccount.registerBankAccount(bankAccountId);
        return bankAccount;
    }

    public static Optional<BankAccount> loadBankAccount(String bankAccountId, EventStore eventStore) {
        List<Event> events = eventStore.load(bankAccountId);
        if (events.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new BankAccount(bankAccountId, eventStore, events));
    }

    private static final Logger logger = LoggerFactory.getLogger(BankAccount.class);

    private final InnerEventListener eventProcessor = new InnerEventListener();

    private final EventStore eventStore;

    private String id;

    private int creditBalance;

    private int version;

    private final Map<String, TransferRequested> pendingTransfers;

    private BankAccount(EventStore eventStore) {
        this(null, eventStore, new ArrayList<>());
    }

    private BankAccount(String id, EventStore eventStore, List<Event> events) {
        this(id, eventStore, 0, 0);
        events.forEach(this::applyEvent);
    }

    @VisibleForTesting
    BankAccount(String id,
                EventStore eventStore,
                int creditBalance,
                int aggregateVersion) {
        this(id, eventStore, creditBalance, aggregateVersion, new HashMap<>());
    }

    @VisibleForTesting
    BankAccount(String id,
                EventStore eventStore,
                int creditBalance,
                int aggregateVersion,
                Map<String, TransferRequested> pendingTransfers) {
        this.id = id;
        this.eventStore = eventStore;
        this.creditBalance = creditBalance;
        this.version = aggregateVersion;
        this.pendingTransfers = pendingTransfers;
    }

    @DecisionFunction
    private void registerBankAccount(String bankAccountId) {
        eventStore.save(version, new BankAccountRegistered(bankAccountId))
                  .forEach(this::applyEvent);
    }

    @DecisionFunction
    public void provisionCredit(int creditToProvision) {
        eventStore.save(version, new CreditProvisioned(id, creditToProvision, creditBalance + creditToProvision))
                  .forEach(this::applyEvent);
    }

    @DecisionFunction
    public void withdrawCredit(int creditToWithdraw) {
        int newCreditBalance = creditBalance - creditToWithdraw;
        if (newCreditBalance < 0) {
            logger.info("not enough credit ({}) to withdraw {}", creditBalance, creditToWithdraw);
            throw new InvalidCommandException();
        }

        eventStore.save(version, new CreditWithdrawn(id, creditToWithdraw, newCreditBalance))
                  .forEach(this::applyEvent);
    }

    @DecisionFunction
    public String requestTransfer(String bankAccountDestinationId, int creditToTransfer) {
        if (bankAccountDestinationId.equals(id)) {
            logger.info("cannot transfer {} credit to same account {}", creditToTransfer, bankAccountDestinationId);
            throw new InvalidCommandException();
        }

        int newCreditBalance = creditBalance - creditToTransfer;
        if (newCreditBalance < 0) {
            logger.info("not enough credit ({}) to transfer {}", creditBalance, creditToTransfer);
            throw new InvalidCommandException();
        }

        String transferId = generateTransferId();
        eventStore.save(version, new TransferRequested(id,
                                                       transferId,
                                                       bankAccountDestinationId,
                                                       creditToTransfer,
                                                       newCreditBalance))
                  .forEach(this::applyEvent);

        return transferId;
    }

    @DecisionFunction
    public void receiveTransfer(String bankAccountOriginId, String transferId, int creditTransferred) {
        eventStore.save(version, new TransferReceived(id,
                                                      transferId,
                                                      bankAccountOriginId,
                                                      creditTransferred,
                                                      creditBalance + creditTransferred))
                  .forEach(this::applyEvent);
    }

    @DecisionFunction
    public void completeTransfer(String transferId) {
        TransferRequested transferRequested = pendingTransfers.get(transferId);
        if (transferRequested == null) {
            logger.info("transfer designed by id {} has not been requested or was already completed", transferId);
            throw new InvalidCommandException();
        }

        eventStore.save(version, new TransferCompleted(id,
                                                       transferId,
                                                       transferRequested.bankAccountIdDestination))
                  .forEach(this::applyEvent);
    }

    @DecisionFunction
    public void cancelTransfer(String transferId) {
        if (!pendingTransfers.containsKey(transferId)) {
            logger.info("transfer designed by id {} has not been requested or was already completed", transferId);
            throw new InvalidCommandException();
        }

        TransferRequested transferRequested = pendingTransfers.get(transferId);
        eventStore.save(version, new TransferCanceled(id,
                                                      transferId,
                                                      transferRequested.bankAccountIdDestination,
                                                      transferRequested.creditTransferred,
                                                      creditBalance + transferRequested.creditTransferred))
                  .forEach(this::applyEvent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(creditBalance, that.creditBalance) &&
                Objects.equals(version, that.version) &&
                Objects.equals(pendingTransfers, that.pendingTransfers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", creditBalance=" + creditBalance +
                ", version=" + version +
                ", pendingTransfers=" + pendingTransfers +
                '}';
    }

    private void applyEvent(Event event) {
        event.applyOn(eventProcessor);
    }

    private String generateTransferId() {
        return randomUUID().toString();
    }

    private class InnerEventListener implements EventListener {

        @Override
        @EvolutionFunction
        public void on(BankAccountRegistered bankAccountRegistered) {
            id = bankAccountRegistered.aggregateId;
            version++;
        }

        @Override
        @EvolutionFunction
        public void on(CreditProvisioned creditProvisioned) {
            creditBalance = creditProvisioned.newCreditBalance;
            version++;
        }

        @Override
        @EvolutionFunction
        public void on(CreditWithdrawn creditWithdrawn) {
            creditBalance = creditWithdrawn.newCreditBalance;
            version++;
        }

        @Override
        @EvolutionFunction
        public void on(TransferRequested transferRequested) {
            creditBalance = transferRequested.newCreditBalance;
            pendingTransfers.put(transferRequested.transferId, transferRequested);
            version++;
        }

        @Override
        @EvolutionFunction
        public void on(TransferReceived transferReceived) {
            creditBalance = transferReceived.newCreditBalance;
            version++;
        }

        @Override
        @EvolutionFunction
        public void on(TransferCompleted transferCompleted) {
            pendingTransfers.remove(transferCompleted.transferId);
            version++;
        }

        @Override
        @EvolutionFunction
        public void on(TransferCanceled transferCanceled) {
            pendingTransfers.remove(transferCanceled.transferId);
            creditBalance = transferCanceled.newCreditBalance;
            version++;
        }
    }

}
