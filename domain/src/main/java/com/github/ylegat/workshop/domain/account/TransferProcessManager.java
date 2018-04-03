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
        /*
          1. load the transfer destination bank account (BankAccount.loadBankAccount(...))
          2. if the account exist, send a command to it for receiving the transfer
          3. if it does not exist, load the transfer origin account and send a command for canceling the transfer
          3. if the origin account does not exist, or if any exception is thrown by any command, log an error
         */
    }

    @Override
    public void on(TransferReceived transferReceived) {
        /*
          1. load the transfer origin bank account
          2. if the account exist, send a command for completing the transfer
          3. if the account does not exist, or if any exception is thrown by any command, log an error
         */
    }

    @Override
    public void on(TransferCompleted transferCompleted) {
        /*
          TransferCompleted event is ignored by the transfer process manager
         */
    }

    @Override
    public void on(TransferCanceled transferCanceled) {
        /*
          TransferCanceled event is ignored by the transfer process manager
         */
    }
}
