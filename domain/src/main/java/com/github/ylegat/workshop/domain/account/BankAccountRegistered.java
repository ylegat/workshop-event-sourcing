package com.github.ylegat.workshop.domain.account;

import com.github.ylegat.workshop.domain.common.Event;
import com.github.ylegat.workshop.domain.common.EventListener;

public class BankAccountRegistered extends Event {

    public BankAccountRegistered(String bankAccountId) {
        super(bankAccountId);
    }

    @Override
    public void applyOn(EventListener eventListener) {
        eventListener.on(this);
    }

    @Override
    public String toString() {
        return "BankAccountRegistered{} " + super.toString();
    }
}
