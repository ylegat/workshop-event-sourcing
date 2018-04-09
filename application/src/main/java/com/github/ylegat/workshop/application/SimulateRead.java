package com.github.ylegat.workshop.application;

import static java.lang.String.format;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class SimulateRead {

    public static final int numberOfAccount = 10;

    public static void main(String[] args) {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);

                clearScreen();

                int totalBalance = 0;
                for (int i = 0; i < numberOfAccount; i++) {
                    String balance = readBalance("account_" + i);
                    System.out.println(balance);
                    if (!balance.equals("command invalid")) {
                        totalBalance += Integer.parseInt(balance.split(" ")[2]);
                    }
                }

                System.out.println("total balance: " + totalBalance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
    }

    private static String readBalance(String id) throws IOException, InterruptedException {
        return execute("GET", "http://localhost:8080/account/" + id + "/balance");
    }

    private static String execute(String method, String url) throws IOException, InterruptedException {
        Process process = new ProcessBuilder().command("sh", "-c", format("curl -s -X%s %s", method, url))
                                              .start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        process.waitFor();
        return reader.readLine();
    }
}
