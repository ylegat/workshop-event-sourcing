package com.github.ylegat.workshop.projection;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ylegat.workshop.domain.account.TransferRequested;
import org.junit.Before;
import org.junit.Test;
import com.github.ylegat.workshop.domain.account.BankAccountRegistered;
import com.github.ylegat.workshop.domain.account.CreditProvisioned;
import com.github.ylegat.workshop.domain.account.CreditWithdrawn;
import com.github.ylegat.workshop.domain.account.TransferCanceled;
import com.github.ylegat.workshop.domain.account.TransferReceived;
import com.github.ylegat.workshop.projection.balance.BalanceProjectionManager;
import com.github.ylegat.workshop.projection.balance.BalanceRepository;
import com.github.ylegat.workshop.projection.balance.InMemoryBalanceRepository;

public class BalanceProjectionManagerTest {

    private final BalanceProjectionManager balanceProjectionManager;

    private final BalanceRepository balanceRepository;

    public BalanceProjectionManagerTest() {
        balanceRepository = new InMemoryBalanceRepository();
        balanceProjectionManager = new BalanceProjectionManager(balanceRepository);
    }

    @Before
    public void before() {
        balanceRepository.clear();
    }

    @Test
    public void onBankAccountRegistered() {
        // When
        balanceProjectionManager.on(new BankAccountRegistered("bankAccountId"));

        // Then
        assertThat(balanceRepository.readBalance("bankAccountId")).contains(0);
    }

    @Test
    public void onCreditProvisioned() {
        // When
        balanceProjectionManager.on(new CreditProvisioned("bankAccountId", 10, 15));

        // Then
        assertThat(balanceRepository.readBalance("bankAccountId")).contains(15);
    }

    @Test
    public void onCreditWithdrawn() {
        // When
        balanceProjectionManager.on(new CreditWithdrawn("bankAccountId", 10, 15));

        // Then
        assertThat(balanceRepository.readBalance("bankAccountId")).contains(15);
    }

    @Test
    public void onTransferRequested() {
        // When
        balanceProjectionManager.on(new TransferRequested("bankAccountId", "transferId",
                                                          "bankAccountDestination",
                                                          10,
                                                          15));

        // Then
        assertThat(balanceRepository.readBalance("bankAccountId")).contains(15);
    }

    @Test
    public void onTransferReceived() {
        // When
        balanceProjectionManager.on(new TransferReceived("bankAccountId",
                                                         "transferId",
                                                         "bankAccountDestination",
                                                         10,
                                                         15));

        // Then
        assertThat(balanceRepository.readBalance("bankAccountId")).contains(15);
    }

    @Test
    public void onTransferCanceled() {
        // When
        balanceProjectionManager.on(new TransferCanceled("bankAccountId",
                                                         "transferId",
                                                         "bankAccountDestination",
                                                         10,
                                                         15));

        // Then
        assertThat(balanceRepository.readBalance("bankAccountId")).contains(15);
    }

}
