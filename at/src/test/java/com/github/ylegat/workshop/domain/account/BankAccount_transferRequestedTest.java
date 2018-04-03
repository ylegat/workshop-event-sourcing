package com.github.ylegat.workshop.domain.account;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import org.junit.Test;
import com.github.ylegat.workshop.domain.common.InvalidCommandException;

public class BankAccount_transferRequestedTest extends AbstractBankAccountTesting {

    @Test
    public void should_fail_requesting_transfer_when_destination_is_same_than_origin() {
        // Given
        BankAccount bankAccountOrigin = registerBankAccount("bankAccountOriginId", eventStore);

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccountOrigin.requestTransfer("bankAccountOriginId",
                                                                                                   1));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"));
    }

    @Test
    public void should_fail_requesting_transfer_when_credit_to_transfer_greater_than_available_credit() {
        // Given
        BankAccount bankAccountOrigin = registerBankAccount("bankAccountOriginId", eventStore);

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccountOrigin.requestTransfer("bankAccountDestinationId",
                                                                                                   1));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"));
    }

    @Test
    public void should_request_transfer() {
        // Given
        BankAccount bankAccountOrigin = registerBankAccount("bankAccountOriginId", eventStore);
        bankAccountOrigin.provisionCredit(1);

        // When
        String transferId = bankAccountOrigin.requestTransfer("bankAccountDestinationId", 1);

        // Then
        assertThat(transferId).isNotNull();

        TransferRequested transferRequested = new TransferRequested("bankAccountOriginId",
                                                                    transferId,
                                                                    "bankAccountDestinationId",
                                                                    1,
                                                                    0);

        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"),
                                                                new CreditProvisioned("bankAccountOriginId", 1, 1),
                                                                transferRequested);

        assertThat(bankAccountOrigin).isEqualTo(new BankAccount("bankAccountOriginId",
                                                                eventStore,
                                                                0,
                                                                3,
                                                                singletonMap(transferId, transferRequested)));
    }

}
