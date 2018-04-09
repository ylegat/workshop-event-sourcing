package com.github.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import org.junit.Test;
import com.github.ylegat.workshop.domain.common.InvalidCommandException;

public class BankAccount_transferCanceledTest extends AbstractBankAccountTesting {

    @Test
    public void should_fail_canceling_transfer_never_requested() {
        // Given
        BankAccount bankAccount = registerBankAccount("bankAccountId", eventStore);

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccount.cancelTransfer("transferId"));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"));
    }

    @Test
    public void should_cancel_pending_transaction_with_success() {
        // Given
        BankAccount bankAccountOrigin = registerBankAccount("bankAccountOriginId", eventStore);
        bankAccountOrigin.provisionCredit(1);

        String transferId = bankAccountOrigin.requestTransfer("bankAccountDestinationId", 1);

        // When
        bankAccountOrigin.cancelTransfer(transferId);

        // Then
        assertThatEvents("bankAccountOriginId").containsOnly(new BankAccountRegistered("bankAccountOriginId"),
                                                             new CreditProvisioned("bankAccountOriginId", 1, 1),
                                                             new TransferRequested("bankAccountOriginId", transferId,
                                                                                   "bankAccountDestinationId",
                                                                                   1,
                                                                                   0),
                                                             new TransferCanceled("bankAccountOriginId",
                                                                                  transferId,
                                                                                  "bankAccountDestinationId",
                                                                                  1,
                                                                                  1));

        assertThat(bankAccountOrigin).isEqualTo(new BankAccount("bankAccountOriginId", eventStore, 1, 4));
    }

    @Test
    public void should_fail_canceling_already_canceled_transaction() {
        // Given
        BankAccount bankAccountOrigin = registerBankAccount("bankAccountOriginId", eventStore);
        bankAccountOrigin.provisionCredit(1);

        String transferId = bankAccountOrigin.requestTransfer("bankAccountDestinationId", 1);

        bankAccountOrigin.cancelTransfer(transferId);

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccountOrigin.cancelTransfer(transferId));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"),
                                                                new CreditProvisioned("bankAccountOriginId", 1, 1),
                                                                new TransferRequested("bankAccountOriginId",
                                                                                      transferId,
                                                                                      "bankAccountDestinationId",
                                                                                      1,
                                                                                      0),
                                                                new TransferCanceled("bankAccountOriginId",
                                                                                     transferId,
                                                                                     "bankAccountDestinationId",
                                                                                     1,
                                                                                     1));
    }

}
