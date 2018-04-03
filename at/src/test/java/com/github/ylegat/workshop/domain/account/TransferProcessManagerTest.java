package com.github.ylegat.workshop.domain.account;

import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TransferProcessManagerTest extends AbstractBankAccountTesting {

    @Test
    public void should_complete_transfer_when_destination_exist() {
        fail("fix me");

        // Given
        /*
          1. a transfer process manager registered with the event bus (the event bus is accessible from the superclass)
          2. a registration of the process manager into the event bus (super.eventBus.register(...))
          3. a bank account ("origin") registered and provisioned with 1 credit
          4. a bank account ("destination") registered
         */

        // When
        /*
          when a transfer is initialized from "origin" to "destination"
         */

        // Then
        /*
          1. "origin" events should contain exactly 1 BankAccountRegistered, 1 CreditProvisioned, 1 TransferRequested and 1 TransferCompleted
          2. "destinations" events should contain exactly 1 BankAccountRegistered and 1 TransferReceived
         */
    }

    @Test
    public void should_cancel_transfer_when_destination_does_not_exist() {
        fail("fix me");

        // Given
        /*
          1. a transfer process manager registered with the event bus (the event bus is accessible from the superclass)
          2. a registration of the process manager into the event bus (super.eventBus.register(...))
          3. a bank account ("origin") registered and provisioned with 1 credit
         */

        // When
        /*
          when a transfer is initialized from "origin" to a non registered bank account id
         */

        // Then
        /*
          1. "origin" events should contains exactly 1 BankAccountRegistered, 1 CreditProvisioned, 1 TransferEmitted and 1 TransferCanceled
         */
    }

}
