package com.github.ylegat.workshop.domain.account;

import static com.github.ylegat.workshop.domain.account.BankAccount.registerBankAccount;
import org.junit.Test;

public class TransferProcessManagerTest extends AbstractBankAccountTesting {

    @Test
    public void should_complete_transfer() {
        // Given
        TransferProcessManager transferProcessManager = new TransferProcessManager(eventStore);
        eventBus.register(transferProcessManager);

        registerBankAccount("bankAccountDestinationId", eventStore);

        BankAccount bankAccountOrigin = registerBankAccount("bankAccountOriginId", eventStore);
        bankAccountOrigin.provisionCredit(1);

        // When
        String transferId = bankAccountOrigin.requestTransfer("bankAccountDestinationId", 1);

        // Then
        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"),
                                                                new CreditProvisioned("bankAccountOriginId", 1, 1),
                                                                new TransferRequested("bankAccountOriginId",
                                                                                      transferId,
                                                                                      "bankAccountDestinationId",
                                                                                      1,
                                                                                      0),
                                                                new TransferCompleted("bankAccountOriginId",
                                                                                      transferId,
                                                                                      "bankAccountDestinationId"));

        assertThatEvents("bankAccountDestinationId").containsExactly(new BankAccountRegistered("bankAccountDestinationId"),
                                                                     new TransferReceived("bankAccountDestinationId",
                                                                                          transferId,
                                                                                          "bankAccountOriginId",
                                                                                          1,
                                                                                          1));
    }

    @Test
    public void should_cancel_transfer() {
        // Given
        TransferProcessManager transferProcessManager = new TransferProcessManager(eventStore);
        eventBus.register(transferProcessManager);

        BankAccount bankAccountOrigin = registerBankAccount("bankAccountOriginId", eventStore);
        bankAccountOrigin.provisionCredit(1);

        // When
        String transferId = bankAccountOrigin.requestTransfer("bankAccountDestinationId", 1);

        // Then
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
