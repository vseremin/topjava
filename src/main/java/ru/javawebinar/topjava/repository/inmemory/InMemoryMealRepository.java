package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> mealRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        save(new Meal(LocalDateTime.of(2023, Month.JANUARY, 30, 10, 0), "Еда пользователя 2", 5000), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!mealRepository.containsKey(userId)) {
            mealRepository.put(userId, new ConcurrentHashMap<>());
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealRepository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealRepository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meals = mealRepository.get(userId);
        if (meals != null) {
            return mealRepository.get(userId).get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId, Predicate<Meal> predicate) {
        Map<Integer, Meal> meals = mealRepository.get(userId);
        return meals == null ? Collections.emptyList() : meals.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}

