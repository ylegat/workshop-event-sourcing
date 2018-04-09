package com.github.ylegat.workshop.projection.balance;

import java.util.Optional;

public interface BalanceRepository {

    void writeCreditBalance(String bankAccountId, int credit);

    Optional<Integer> readBalance(String bankAccountId);

    void clear();
}
