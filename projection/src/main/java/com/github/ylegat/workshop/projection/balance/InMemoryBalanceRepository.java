package com.github.ylegat.workshop.projection.balance;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBalanceRepository implements BalanceRepository {

    private final Map<String, Integer> balance = new ConcurrentHashMap<>();

    @Override
    public void writeCreditBalance(String bankAccountId, int credit) {
        balance.put(bankAccountId, credit);
    }

    @Override
    public Optional<Integer> readBalance(String bankAccountId) {
        return Optional.ofNullable(balance.get(bankAccountId));
    }

    @Override
    public void clear() {
        balance.clear();
    }

}
