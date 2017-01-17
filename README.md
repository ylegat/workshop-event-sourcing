**step 1: simple command**

```
git merge --no-edit origin/step1/exercise/simple-command
```

We want to implement the 3 basic commands that can be applied on a bank account: registration (creation),  provisioning and withdraw

1. implement account registration command
 * write BankAccount_registerBankAccountTest.should_register_bank_account_with_success test and make it pass
 * write BankAccount_registerBankAccountTest.should_fail_registering_bank_account_with_already_used_id and make it pass
2. implement provisioning command, using BankAccount_provisionCreditTest as a guideline
3. implement withdraw command, using BankAccount_withdrawCreditTest as a guideline


**step 2: long running process**

* if you managed to make all the tests pass:
```
git add .
git commit -m "solution"
git merge --no-edit origin/step2/exercise/long-running-process
```

* if you want to continue from a clean solution
```
git add .
git reset --hard origin/master
git merge --no-edit origin/step1/solution/simple-command
git merge --no-edit origin/step2/exercise/long-running-process
```

A transfer is a long running process: an operation that is decomposed into multiple simple commands, orchestrated by a process manager.
The process manager dedicated to a transfer is named TransferProcessManager.

Commands related to this operation are: request transfer, receive transfer, cancel transfer and complete transfer
All commands have already been implemented except the first one.

1. implement the transfer process manager
 * write TransferProcessManagerTest.should_cancel_transfer_when_destination_does_not_exist test and make it pass
 * write TransferProcessManagerTest.should_complete_transfer_when_destination_exist test and make it pass


**step 3: projection**

* if you managed to make all the tests pass:
```
git add .
git commit -m "solution"
git merge --no-edit origin/step3/exercise/projection
```

* if you want to continue from a clean solution
```
git add .
git reset --hard origin/master
git merge --no-edit origin/step2/solution/long-running-process
git merge --no-edit origin/step3/exercise/projection
```

We need a read model containing the total balance of the accounts.
For this, we use a in memory repository named InMemoryBalanceRepository (already implemented)

We need to implement the projection manager, BalanceProjectionManager, that will be notified when an event is saved so that it can update the total balances using the repository.

1. implement the credit balance projection manager
 * make the tests defined in BalanceProjectionManagerTest pass


**step 4 (optional): create a projection of your own**
