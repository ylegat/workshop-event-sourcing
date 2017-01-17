package com.github.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import org.junit.Test;
import com.github.ylegat.workshop.domain.common.ConflictingEventException;

public class BankAccount_registerBankAccountTest extends AbstractBankAccountTesting {

    @Test
    public void should_register_bank_account_with_success() {
        // When
        BankAccount bankAccount = registerBankAccount("bankAccountId", eventStore);

        // Then
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"));

        assertThat(bankAccount).isEqualTo(new BankAccount("bankAccountId", eventStore, 0, 1));
    }

    @Test
    public void should_fail_registering_bank_account_with_already_used_id() {
        // Given
        registerBankAccount("bankAccountId", eventStore);

        // When
        Throwable conflictingEventException = catchThrowable(() -> registerBankAccount("bankAccountId", eventStore));

        // Then
        assertThat(conflictingEventException).isInstanceOf(ConflictingEventException.class);
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"));
    }

}
