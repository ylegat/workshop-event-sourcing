package com.github.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import org.junit.Test;
import com.github.ylegat.workshop.domain.common.InvalidCommandException;

public class BankAccount_transferFinalizedTest extends AbstractBankAccountTesting {

    @Test
    public void should_fail_completing_transfer_never_requested() {
        // Given
        BankAccount bankAccount = registerBankAccount("bankAccountId", eventStore);

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccount.completeTransfer("transferId"));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"));
    }

    @Test
    public void should_complete_pending_transaction_with_success() {
        // Given
        BankAccount bankAccountOrigin = registerBankAccount("bankAccountOriginId", eventStore);
        bankAccountOrigin.provisionCredit(1);

        String transferId = bankAccountOrigin.requestTransfer("bankAccountDestinationId", 1);

        // When
        bankAccountOrigin.completeTransfer(transferId);

        // Then
        assertThatEvents("bankAccountOriginId").containsOnly(new BankAccountRegistered("bankAccountOriginId"),
                                                             new TransferRequested("bankAccountOriginId", transferId,
                                                                                   "bankAccountDestinationId",
                                                                                   1,
                                                                                   0),
                                                             new TransferCompleted("bankAccountOriginId",
                                                                                   transferId,
                                                                                   "bankAccountDestinationId"));

        assertThat(bankAccountOrigin).isEqualTo(new BankAccount("bankAccountOriginId", eventStore, 0, 4));
    }

    @Test
    public void should_fail_completing_already_finalized_transaction() {
        // Given
        BankAccount bankAccountOrigin = registerBankAccount("bankAccountOriginId", eventStore);
        bankAccountOrigin.provisionCredit(1);

        String transferId = bankAccountOrigin.requestTransfer("bankAccountDestinationId", 1);

        bankAccountOrigin.completeTransfer(transferId);

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccountOrigin.completeTransfer(transferId));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
    }

}
