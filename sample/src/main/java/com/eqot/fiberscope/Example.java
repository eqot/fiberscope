package com.eqot.fiberscope;

import com.eqot.fiberscope.processor.Accessible;
import com.eqot.fiberscope.processor.Fiberscope;

@Fiberscope
public class Example {
    @Accessible
    private int add(int value1, int value2) {
        return value1 + value2;
    }

    @Accessible
    private int sub(int value1, int value2) {
        return value1 - value2;
    }
}
