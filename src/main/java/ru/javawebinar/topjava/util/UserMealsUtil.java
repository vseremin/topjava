package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycle(meals, LocalTime.of(7, 0), LocalTime.of(15, 0), 2005);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStream(meals, LocalTime.of(7, 0), LocalTime.of(15, 0), 2005));
    }

    public static List<UserMealWithExcess> filteredByCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDate = new HashMap<>();
        Map<LocalDate, AtomicBoolean> excessByDays = new HashMap<>();
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();

        meals.forEach(meal -> {
            LocalDate date = meal.getDateTime().toLocalDate();
            caloriesPerDate.merge(date, meal.getCalories(), Integer::sum);
            excessByDays.computeIfAbsent(date, excess -> new AtomicBoolean())
                    .set(caloriesPerDate.get(date) > caloriesPerDay);

            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        excessByDays.get(meal.getDateTime().toLocalDate())));
            }
        });
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(groupingBy(meal -> meal.getDateTime().toLocalDate()))
                .values().stream()
                .flatMap(userMeals -> {
                    AtomicBoolean excess = new AtomicBoolean(userMeals.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay);
                    return userMeals.stream()
                            .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                            .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
                }).collect(Collectors.toList());
    }
}

