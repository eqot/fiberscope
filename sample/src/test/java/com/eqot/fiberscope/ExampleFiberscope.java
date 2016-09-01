package com.eqot.fiberscope;

import java.lang.reflect.Method;

public class ExampleFiberscope {
    private final Example mInstance;

    private Method mMethodAdd;

    public ExampleFiberscope() {
        mInstance = new Example();

        try {
            mMethodAdd = Example.class.getDeclaredMethod("add", int.class, int.class);
            mMethodAdd.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int add(int value1, int value2) {
        int result = 0;
        try {
            result = (int) mMethodAdd.invoke(mInstance, value1, value2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Object add(Object... args) {
        Object result = null;
        try {
            result = mMethodAdd.invoke(mInstance, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
