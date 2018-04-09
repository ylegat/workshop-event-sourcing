package com.github.ylegat.workshop.projection;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import com.github.ylegat.workshop.projection.balance.BalanceRepository;
import com.github.ylegat.workshop.projection.balance.InMemoryBalanceRepository;

public class InMemoryBalanceRepositoryTest {

    private final BalanceRepository balanceRepository;

    public InMemoryBalanceRepositoryTest() {
        this.balanceRepository = new InMemoryBalanceRepository();
    }

    @Before
    public void before() {
        balanceRepository.clear();
    }

    @Test
    public void should_write_credit() {
        // When
        balanceRepository.writeCreditBalance("bankAccountId", 10);

        // Then
        assertThat(balanceRepository.readBalance("bankAccountId")).contains(10);
    }

    @Test
    public void should_update_credit() {
        // When
        balanceRepository.writeCreditBalance("bankAccountId", 10);

        // When
        balanceRepository.writeCreditBalance("bankAccountId", 15);

        // Then
        assertThat(balanceRepository.readBalance("bankAccountId")).contains(15);
    }

}
