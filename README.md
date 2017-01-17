**step 0: init workshop**
```
git clone https://github.com/ylegat/workshop-event-sourcing.git
git checkout step0/init-workshop
```

**step 1: simple command**
```
git merge origin/step1/exercise/simple-command
```

1. implement account registration command
 * write BankAccount_registerBankAccountTest.should_register_bank_account_with_success test and make it pass
 * write BankAccount_registerBankAccountTest.should_fail_registering_bank_account_with_already_used_id and make it pass
2. implement provisioning command, using BankAccount_provisionCreditTest as a guideline
3. implement withdraw command, using BankAccount_withdrawCreditTest as a guideline

```
git add .
git commit
```


**step 2: long running process**

* if you managed to make all the test pass:
```
git merge origin/step2/exercise/long-running-process
```

* if you want to continue from a clean solution
```
git checkout step1/solution/simple-command
```

1. implement transfer request command, using BankAccount_transferRequestedTest as a guideline
2. implement transfer reception command, BankAccount_transferReceivedTest as a guideline
3. implement transfer completion command, using BankAccount_transferCompletedTest as a guideline
4. implement transfer cancellation command, using BankAccount_transferCompletedTest as a guideline
5. implement the transfer process manager
 * write TransferProcessManagerTest.should_complete_transfer test and make it pass
 * write TransferProcessManagerTest.should_cancel_transfer test and make it pass

```
git add .
git commit
```

**step 3: projection**

* if you managed to make all the test pass:
```
git merge origin/step3/exercise/projection
```

* if you want to continue from a clean solution
```
git checkout step3/solution/projection
```

1. implement the credit balance projection manager
 * make the tests defined in BalanceProjectionManagerTest pass

```
git add .
git commit
```

**step 4: integration**

* if you managed to make all the test pass:
```
git merge origin/step4/exercise/integration
```

* if you want to continue from a clean solution
```
git checkout step4/solution/integration
```

1. follow the instruction defined in Server constructor
2. execute Server main method
3. execute SimulateOperation main method
4. open a terminal, go into `~/workspace/workshop-event-sourcing/application/target/classes` and execute `java com.zenika.ylegat.workshop.application.SimulateRead`

**step 5: create a projection of your own**
