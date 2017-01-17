package com.github.ylegat.workshop.domain.account;

import java.util.Objects;
import com.github.ylegat.workshop.domain.common.Event;
import com.github.ylegat.workshop.domain.common.EventListener;

public class TransferReceived extends Event {

    public final String transferId;

    public final String bankAccountIdOrigin;

    public final int newCreditBalance;

    public final int creditTransferred;

    public TransferReceived(String bankAccountId,
                            String transferId,
                            String bankAccountIdOrigin,
                            int creditTransferred,
                            int newCreditBalance) {
        super(bankAccountId);
        this.transferId = transferId;
        this.bankAccountIdOrigin = bankAccountIdOrigin;
        this.newCreditBalance = newCreditBalance;
        this.creditTransferred = creditTransferred;
    }

    @Override
    public void applyOn(EventListener eventListener) {
        eventListener.on(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferReceived that = (TransferReceived) o;
        return Objects.equals(transferId, that.transferId) &&
                Objects.equals(bankAccountIdOrigin, that.bankAccountIdOrigin) &&
                Objects.equals(newCreditBalance, that.newCreditBalance) &&
                Objects.equals(creditTransferred, that.creditTransferred);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, bankAccountIdOrigin, newCreditBalance, creditTransferred);
    }

    @Override
    public String toString() {
        return "TransferReceived{" +
                "transferId=" + transferId +
                ", bankAccountIdOrigin=" + bankAccountIdOrigin +
                ", newCredit=" + newCreditBalance +
                ", creditTransferred=" + creditTransferred +
                "} " + super.toString();
    }
}
