package com.quickbus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SampleTest {

    @Test
    void sampleTest() {
        int expected = 10;
        int actual = 5 + 5;
        assertEquals(expected, actual, "Test failed: 5 + 5 should equal 10");
    }
}
