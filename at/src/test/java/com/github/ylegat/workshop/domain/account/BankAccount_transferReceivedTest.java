package com.github.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import org.junit.Test;

public class BankAccount_transferReceivedTest extends AbstractBankAccountTesting {

    @Test
    public void should_receive_transfer() {
        // Given
        BankAccount bankAccountDestination = registerBankAccount("bankAccountDestinationId", eventStore);

        // When
        bankAccountDestination.receiveTransfer("bankAccountOriginId", "transferId", 1);

        // Then
        assertThatEvents("bankAccountDestinationId").containsExactly(new BankAccountRegistered("bankAccountDestinationId"),
                                                                     new TransferReceived("bankAccountDestinationId",
                                                                                          "transferId",
                                                                                          "bankAccountOriginId",
                                                                                          1,
                                                                                          1));

        assertThat(bankAccountDestination).isEqualTo(new BankAccount("bankAccountDestinationId", eventStore, 1, 2));
    }


}
