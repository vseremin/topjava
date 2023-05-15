package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2005);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2005));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDate = new HashMap<>();
        Map<LocalDate, BooleanContainer> localDateBooleanMap = new HashMap<>();
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();

        meals.forEach(meal -> {
            LocalDate date = meal.getDateTime().toLocalDate();
            caloriesPerDate.merge(date, meal.getCalories(), Integer::sum);
            localDateBooleanMap.merge(date, new BooleanContainer(caloriesPerDate.get(date) < caloriesPerDay),
                    (a, b) -> a.getExcess(caloriesPerDate.get(date) < caloriesPerDay));

            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        localDateBooleanMap.get(meal.getDateTime().toLocalDate())));
            }
        });
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDate = new HashMap<>();
        Map<LocalDate, BooleanContainer> localDateBooleanMap = new HashMap<>();

        return meals.stream().map(meal -> {
            LocalDate date = meal.getDateTime().toLocalDate();
            caloriesPerDate.merge(date, meal.getCalories(), Integer::sum);
            localDateBooleanMap.merge(date, new BooleanContainer(caloriesPerDate.get(date) < caloriesPerDay),
                    (a, b) -> a.getExcess(caloriesPerDate.get(date) < caloriesPerDay));
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        localDateBooleanMap.get(date));
            }
            return null;
        }).filter(meal -> meal != null).collect(Collectors.toList());
    }
}
