package com.github.ylegat.workshop.application;

import static java.lang.String.format;
import java.io.IOException;
import java.util.Random;

public class SimulateOperation {

    public static final int numberOfAccount = 10;

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < numberOfAccount; i++) {
            createAccount("account_" + i);
            provision("account_" + i, 1_000);
        }

        while(true) {
            Random random = new Random();
            String fromId = "account_" + random.nextInt(numberOfAccount);
            String toId = "account_" + random.nextInt(numberOfAccount);

            transfer(fromId, toId, 10);
        }
    }

    private static void createAccount(String id) throws IOException, InterruptedException {
        execute("POST", "http://localhost:8080/account/" + id);
    }

    private static void provision(String id, int amount) throws IOException, InterruptedException {
        execute("POST", "http://localhost:8080/account/" + id +"/provision/" + amount);
    }

    private static void transfer(String fromId, String toId, int amount) throws IOException, InterruptedException {
        execute("POST", "http://localhost:8080/account/" + fromId + "/transfer/" + amount + "/to/" + toId);
    }

    private static void execute(String method, String url) throws IOException, InterruptedException {
        Process process =  new ProcessBuilder().inheritIO()
                                                .command("sh", "-c", format("curl -s -X%s %s", method, url))
                                                .start();

        process.waitFor();
        System.out.print("\n");
    }
}
