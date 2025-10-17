package com.cyrilng.jmh;

import java.io.IOException;

/**
 * Start the JMH benchmark runner.
 */
public class BenchmarkRunner {
    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }
}