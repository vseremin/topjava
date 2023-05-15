package ru.javawebinar.topjava.util;

public class BooleanContainer {
    private boolean excess;

    public BooleanContainer(boolean excess) {
        this.excess = excess;
    }

    public boolean isExcess() {
        return excess;
    }

    public void setExcess(boolean excess) {
        excess = excess;
    }

    public BooleanContainer getExcess(boolean excess) {
        this.excess = excess;
        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(excess);
    }
}
