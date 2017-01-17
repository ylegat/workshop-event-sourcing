package com.github.ylegat.workshop.domain.account;

import java.util.Objects;
import com.github.ylegat.workshop.domain.common.Event;
import com.github.ylegat.workshop.domain.common.EventListener;

public class TransferCanceled extends Event {

    public final String transferId;

    public final String bankAccountIdDestination;

    public final int newCreditBalance;

    public final int creditRefund;

    public TransferCanceled(String bankAccountId,
                            String transferId,
                            String bankAccountIdDestination,
                            int creditRefund,
                            int newCreditBalance) {
        super(bankAccountId);
        this.transferId = transferId;
        this.bankAccountIdDestination = bankAccountIdDestination;
        this.newCreditBalance = newCreditBalance;
        this.creditRefund = creditRefund;
    }

    @Override
    public void applyOn(EventListener eventListener) {
        eventListener.on(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferCanceled that = (TransferCanceled) o;
        return Objects.equals(transferId, that.transferId) &&
                Objects.equals(bankAccountIdDestination, that.bankAccountIdDestination) &&
                Objects.equals(newCreditBalance, that.newCreditBalance) &&
                Objects.equals(creditRefund, that.creditRefund);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, bankAccountIdDestination, newCreditBalance, creditRefund);
    }

    @Override
    public String toString() {
        return "TransferCanceled{" +
                "transferId=" + transferId +
                ", bankAccountIdDestination=" + bankAccountIdDestination +
                ", newCredit=" + newCreditBalance +
                ", creditRefund=" + creditRefund +
                "} " + super.toString();
    }
}
