package com.github.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import static org.assertj.core.api.Fail.fail;
import org.junit.Test;
import com.github.ylegat.workshop.domain.common.ConflictingEventException;

public class BankAccount_registerBankAccountTest extends AbstractBankAccountTesting {

    @Test
    public void should_register_bank_account_with_success() {
        fail("Not implemented");
        // When
        /*
          when a bank account is registered (BankAccount.registerBankAccount, use the eventStore instantiated in the parent class)
         */

        // Then
        /*
          1. assert that the events associated to the bank account contains exactly one BankAccountRegistered event (use assertThatEvents method defined in the superclass)
          * assertThatEvents(...).containsExactly(...)
          2. assert on the state of the bank account (you can use Assertion.assertThat(actualBankAccount).isEqualTo(expectedBankAccount)) :
          * it's id should be identical to the one created
          * its credit should be equal to 0
          * its version should be 1 (one event has been applied on the bank account)
         */
    }

    @Test
    public void should_fail_registering_bank_account_with_already_used_id() {
        fail("Not implemented");
        // Given
        /*
          Given a bank account registered (BankAccount.registerBankAccount)
         */

        // When
        /*
          When a bank account with the same id is registered (use Assertions.catchThrowable(() -> BankAccount.registerBankAccount(...)) to catch the exception)
          * Throwable throwable = Assertions.catchThrowable(() -> BankAccount.registerBankAccount(...))
         */

        // Then
        /*
          1. assert that the command thrown a ConflictingEventException exception
          * Assertions.assertThat(throwabe).isInstanceOf(...)
          2. assert that the events associated to the bank account contains exactly one BankAccountRegistered event
         */
    }

}
