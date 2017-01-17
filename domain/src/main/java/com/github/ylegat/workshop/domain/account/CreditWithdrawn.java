package com.github.ylegat.workshop.domain.account;

import java.util.Objects;
import com.github.ylegat.workshop.domain.common.Event;
import com.github.ylegat.workshop.domain.common.EventListener;

public class CreditWithdrawn extends Event {

    public final int newCreditBalance;
    public final int creditWithdrawn;

    public CreditWithdrawn(String bankAccountId, int creditWithdrawn, int newCreditBalance) {
        super(bankAccountId);
        this.newCreditBalance = newCreditBalance;
        this.creditWithdrawn = creditWithdrawn;
    }

    @Override
    public void applyOn(EventListener eventListener) {
        eventListener.on(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditWithdrawn that = (CreditWithdrawn) o;
        return Objects.equals(newCreditBalance, that.newCreditBalance) &&
                Objects.equals(creditWithdrawn, that.creditWithdrawn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newCreditBalance, creditWithdrawn);
    }

    @Override
    public String toString() {
        return "CreditWithdrawn{" +
                ", newCredit=" + newCreditBalance +
                ", creditWithdrawn=" + creditWithdrawn +
                "} " + super.toString();
    }
}
