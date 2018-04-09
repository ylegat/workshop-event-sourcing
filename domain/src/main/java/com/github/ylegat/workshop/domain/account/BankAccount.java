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
        /*
          1. instantiate a BankAccountRegistered event
          2. save the event List<Event> savedEvents = eventStore.save(events)
          3. apply saved events on the bank account savedEvents.foreach(this::applyEvent)
         */
    }

    @DecisionFunction
    public void provisionCredit(int creditToProvision) {
        /*
          1. instantiate a CreditProvisioned event
          2. save the event List<Event> savedEvents = eventStore.save(events)
          3. apply saved events on the bank account savedEvents.foreach(this::applyEvent)
         */
    }

    @DecisionFunction
    public void withdrawCredit(int creditToWithdraw) {
        // /!\ creditToWithdraw represent a positive value
        /*
          1. throw an InvalidCommandException if the balance is lower then the credit amount to withdraw
          2. instantiate a CreditWithdrawn event
          3. save the event List<Event> savedEvents = eventStore.save(events)
          4. apply saved events on the bank account savedEvents.foreach(this::applyEvent)
         */
    }

    @DecisionFunction
    public String requestTransfer(String bankAccountDestinationId, int creditToTransfer) {
        String randomTransferId = generateTransferId();
        /*
          1. throw an InvalidCommandException if the bank destination id is the same that this id
          2. throw an InvalidCommandException if the balance is lower then the credit amount to transfer
          3. instantiate a TransferRequest event (you can generate a random transfer id by calling UUID.randomUUID)
          4. save the event List<Event> savedEvents = eventStore.save(events)
          5. apply saved events on the bank account savedEvents.foreach(this::applyEvent)
          6. return the transfer id associated the the transfer
         */
        return null;
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
            /*
              1. affect the event's aggregate id to the bank account's id
              2. increment the aggregate's version
             */
        }

        @Override
        @EvolutionFunction
        public void on(CreditProvisioned creditProvisioned) {
            /*
              1. affect the event's new credit balance to the bank account's balance
              2. increment the aggregate's version
             */
        }

        @Override
        @EvolutionFunction
        public void on(CreditWithdrawn creditWithdrawn) {
            /*
              1. affect the event's new credit balance to the bank account's balance
              2. increment the aggregate's version
             */
        }

        @Override
        @EvolutionFunction
        public void on(TransferRequested transferRequested) {
            /*
              1. affect the event's new credit balance to the bank account's balance
              2. add the event to the pending transfers map
              3. increment the aggregate's version
             */
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
