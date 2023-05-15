package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.BooleanContainer;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final BooleanContainer excess;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, BooleanContainer excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
