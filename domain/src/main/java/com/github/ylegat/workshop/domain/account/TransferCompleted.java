package com.github.ylegat.workshop.domain.account;

import java.util.Objects;
import com.github.ylegat.workshop.domain.common.Event;
import com.github.ylegat.workshop.domain.common.EventListener;

public class TransferCompleted extends Event {

    public final String transferId;

    public final String bankAccountIdDestination;

    public TransferCompleted(String aggregateId, String transferId, String bankAccountIdDestination) {
        super(aggregateId);
        this.transferId = transferId;
        this.bankAccountIdDestination = bankAccountIdDestination;
    }

    @Override
    public void applyOn(EventListener eventListener) {
        eventListener.on(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferCompleted that = (TransferCompleted) o;
        return Objects.equals(transferId, that.transferId) &&
                Objects.equals(bankAccountIdDestination, that.bankAccountIdDestination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, bankAccountIdDestination);
    }

    @Override
    public String toString() {
        return "TransferCompleted{" +
                "transferId=" + transferId +
                ", bankAccountIdDestination=" + bankAccountIdDestination +
                '}';
    }
}
