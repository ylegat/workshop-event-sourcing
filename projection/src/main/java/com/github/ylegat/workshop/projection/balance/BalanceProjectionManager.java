package com.github.ylegat.workshop.projection.balance;

import com.github.ylegat.workshop.domain.account.BankAccountRegistered;
import com.github.ylegat.workshop.domain.account.CreditProvisioned;
import com.github.ylegat.workshop.domain.account.CreditWithdrawn;
import com.github.ylegat.workshop.domain.account.TransferCanceled;
import com.github.ylegat.workshop.domain.account.TransferCompleted;
import com.github.ylegat.workshop.domain.account.TransferRequested;
import com.github.ylegat.workshop.domain.account.TransferReceived;

public class BalanceProjectionManager implements ProjectionManager {

    private final BalanceRepository balanceRepository;

    public BalanceProjectionManager(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public void on(BankAccountRegistered bankAccountRegistered) {
        /*
          project the event by using the balance repository
         */
    }

    @Override
    public void on(CreditProvisioned creditProvisioned) {
        /*
          project the event by using the balance repository
         */
    }

    @Override
    public void on(CreditWithdrawn creditWithdrawn) {
        /*
          project the event by using the balance repository
         */
    }

    @Override
    public void on(TransferRequested transferRequested) {
        /*
          project the event by using the balance repository
         */
    }

    @Override
    public void on(TransferReceived transferReceived) {
        /*
          project the event by using the balance repository
         */
    }

    @Override
    public void on(TransferCompleted transferCompleted) {

    }

    @Override
    public void on(TransferCanceled transferCanceled) {
        /*
          project the event by using the balance repository
         */
    }

}
