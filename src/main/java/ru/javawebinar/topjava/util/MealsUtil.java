package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MapStorageMeal;
import ru.javawebinar.topjava.storage.StorageMeal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    private static final StorageMeal storage = new MapStorageMeal();
    public static final int CALORIES_PER_DAY = 2000;

    public static void main(String[] args) {
        List<Meal> meals = storage.getAll();
        List<MealTo> mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(15, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, int caloriesPerDate) {
        return filteredByStreams(meals, m -> true, caloriesPerDate);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDate) {
        Predicate<Meal> predicate = meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime);
        return filteredByStreams(meals, predicate, caloriesPerDate);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, Predicate<Meal> predicate, int caloriesPerDate) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );
        return meals.stream()
                .filter(predicate)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDate))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
