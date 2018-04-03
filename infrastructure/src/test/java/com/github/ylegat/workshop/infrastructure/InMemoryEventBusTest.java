package com.github.ylegat.workshop.infrastructure;

import com.github.ylegat.workshop.domain.account.BankAccountRegistered;
import com.github.ylegat.workshop.domain.account.CreditProvisioned;
import com.github.ylegat.workshop.domain.account.CreditWithdrawn;
import com.github.ylegat.workshop.domain.account.TransferCanceled;
import com.github.ylegat.workshop.domain.account.TransferCompleted;
import com.github.ylegat.workshop.domain.account.TransferReceived;
import com.github.ylegat.workshop.domain.account.TransferRequested;
import com.github.ylegat.workshop.domain.common.EventListener;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryEventBusTest {

    @Test
    public void should_notify_listener() throws InterruptedException {
        // Given
        InMemoryEventBus eventBus = new InMemoryEventBus();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        eventBus.register(new AbstractEventListener() {
            @Override
            public void on(BankAccountRegistered bankAccountRegistered) {
                countDownLatch.countDown();
            }
        });

        // When
        eventBus.push(singletonList(new BankAccountRegistered("bankAccountId")));
        boolean registrationNotified = countDownLatch.await(100, TimeUnit.MILLISECONDS);

        // Then
        assertThat(registrationNotified).isTrue();
    }

    @Test
    public void should_process_events_in_order() throws InterruptedException {
        // Given
        InMemoryEventBus eventBus = new InMemoryEventBus();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        eventBus.register(new AbstractEventListener() {
            @Override
            public void on(BankAccountRegistered bankAccountRegistered) {
                eventBus.push(singletonList(new CreditProvisioned("bankAccountId", 1, 1)));
            }
        });

        AtomicBoolean registered = new AtomicBoolean();
        AtomicBoolean provisionedAfterRegistered = new AtomicBoolean();
        eventBus.register(new AbstractEventListener() {
            @Override
            public void on(BankAccountRegistered bankAccountRegistered) {
                registered.set(true);
            }

            @Override
            public void on(CreditProvisioned creditProvisioned) {
                provisionedAfterRegistered.set(registered.get());
                countDownLatch.countDown();
            }
        });

        // When
        eventBus.push(singletonList(new BankAccountRegistered("bankAccountId")));
        boolean provisioningNotified = countDownLatch.await(100, TimeUnit.MILLISECONDS);

        // Then
        assertThat(provisioningNotified).isTrue();
        assertThat(provisionedAfterRegistered).isTrue();
    }

    private abstract class AbstractEventListener implements EventListener {

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

        }

        @Override
        public void on(TransferReceived transferReceived) {

        }

        @Override
        public void on(TransferCompleted transferCompleted) {

        }

        @Override
        public void on(TransferCanceled transferCanceled) {

        }
    }
}