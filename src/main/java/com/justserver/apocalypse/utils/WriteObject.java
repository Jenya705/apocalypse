package com.justserver.apocalypse.utils;

public class WriteObject {
    private final boolean primitive;
    private final Object value;

    public WriteObject(boolean primitive, Object value) {
        this.primitive = primitive;
        this.value = value;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public Object getValue() {
        return value;
    }
}