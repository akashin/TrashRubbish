package com.mygdx.util;

public class Accumulator {
    private float bound;
    private float value;

    public Accumulator(float bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Bound must be above zero");
        }
        this.bound = bound;
    }

    public float getBound() {
        return bound;
    }

    public void setBound(float bound) {
        this.bound = bound;
    }

    public void update(float delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("Delta can't be below zero");
        }
        value += delta;
    }

    public boolean ready() {
        return value >= bound;
    }

    public void use() {
        if (value < bound) {
            throw new IllegalStateException("Accumulator is not ready yet");
        }
        value -= bound;
    }

    public boolean useIfReady() {
        if (ready()) {
            use();
            return true;
        } else {
            return false;
        }
    }

    public void restart() {
        value = 0;
    }

    public float getValue() {
        return value;
    }
}
