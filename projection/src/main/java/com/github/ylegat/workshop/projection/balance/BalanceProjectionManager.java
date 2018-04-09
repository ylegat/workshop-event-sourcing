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
        balanceRepository.writeCreditBalance(bankAccountRegistered.aggregateId, 0);
    }

    @Override
    public void on(CreditProvisioned creditProvisioned) {
        balanceRepository.writeCreditBalance(creditProvisioned.aggregateId, creditProvisioned.newCreditBalance);
    }

    @Override
    public void on(CreditWithdrawn creditWithdrawn) {
        balanceRepository.writeCreditBalance(creditWithdrawn.aggregateId, creditWithdrawn.newCreditBalance);
    }

    @Override
    public void on(TransferRequested transferRequested) {
        balanceRepository.writeCreditBalance(transferRequested.aggregateId, transferRequested.newCreditBalance);
    }

    @Override
    public void on(TransferReceived transferReceived) {
        balanceRepository.writeCreditBalance(transferReceived.aggregateId, transferReceived.newCreditBalance);
    }

    @Override
    public void on(TransferCompleted transferCompleted) {

    }

    @Override
    public void on(TransferCanceled transferCanceled) {
        balanceRepository.writeCreditBalance(transferCanceled.aggregateId, transferCanceled.newCreditBalance);
    }

}
