package org.pogonin;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoThreadCounterTest {
    @Test
    public void testStartCounting() {
        TwoThreadCounter counter = new TwoThreadCounter();
        ByteArrayOutputStream actualContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(actualContent));
        String expectedOutput = "pool-1-thread-1: 0\r\n" +
                                "pool-1-thread-2: 1\r\n" +
                                "pool-1-thread-1: 2\r\n" +
                                "pool-1-thread-2: 3\r\n" +
                                "pool-1-thread-1: 4\r\n" +
                                "pool-1-thread-2: 5\r\n" +
                                "pool-1-thread-1: 6\r\n" +
                                "pool-1-thread-2: 7\r\n" +
                                "pool-1-thread-1: 8\r\n";

        try {
            counter.startCounting(0, 8);


            assertEquals(expectedOutput, actualContent.toString());
        } finally {System.setOut(originalOut);}
    }
}
