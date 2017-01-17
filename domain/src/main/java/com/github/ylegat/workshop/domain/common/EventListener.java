package com.github.ylegat.workshop.domain.common;

import static java.lang.String.format;
import com.github.ylegat.workshop.domain.account.BankAccountRegistered;
import com.github.ylegat.workshop.domain.account.CreditProvisioned;
import com.github.ylegat.workshop.domain.account.CreditWithdrawn;
import com.github.ylegat.workshop.domain.account.TransferCanceled;
import com.github.ylegat.workshop.domain.account.TransferCompleted;
import com.github.ylegat.workshop.domain.account.TransferRequested;
import com.github.ylegat.workshop.domain.account.TransferReceived;

public interface EventListener {

    void on(BankAccountRegistered bankAccountRegistered);

    void on(CreditProvisioned creditProvisioned);

    void on(CreditWithdrawn creditWithdrawn);

    void on(TransferRequested transferRequested);

    void on(TransferReceived transferReceived);

    void on(TransferCompleted transferCompleted);

    void on(TransferCanceled transferCanceled);

}
