package com.github.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import org.junit.Test;

public class BankAccount_provisionCreditTest extends AbstractBankAccountTesting {

    @Test
    public void should_provision_credit_with_success() {
        // Given
        BankAccount bankAccount = registerBankAccount("bankAccountId", eventStore);

        // When
        bankAccount.provisionCredit(1);

        // Then
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"),
                                                          new CreditProvisioned("bankAccountId", 1, 1));
        assertThat(bankAccount).isEqualTo(new BankAccount("bankAccountId", eventStore, 1, 2));
    }

}
