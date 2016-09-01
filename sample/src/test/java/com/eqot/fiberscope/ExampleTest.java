package com.eqot.fiberscope;

import com.eqot.fiberscope.processor.Fiberscope;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Fiberscope(Example.class)
public class ExampleTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void add() throws Exception {
        final Example$Fiberscope example = new Example$Fiberscope();
        final int result = example.add(1, 2);
        assertEquals(result, 3);
    }

    @Test
    public void sub() throws Exception {
        final Example$Fiberscope example = new Example$Fiberscope();
        final int result = example.sub(1, 2);
        assertEquals(result, -1);
    }
}
