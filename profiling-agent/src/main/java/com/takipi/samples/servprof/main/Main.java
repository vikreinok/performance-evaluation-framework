package com.takipi.samples.servprof.main;

import java.util.Random;

/**
 *
 */
public class Main {

    public static final int MAX_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random(System.currentTimeMillis());
        Main main = new Main();

        for (int count = 0; count < MAX_COUNT; count++) {
            Long sleepDuration = Long.valueOf(random.nextInt(1000) + 10);
            main.sleep(sleepDuration);
        }
    }

    private void sleep(Long sleepDuration) throws InterruptedException {
        Thread.sleep(sleepDuration);
    }

}
