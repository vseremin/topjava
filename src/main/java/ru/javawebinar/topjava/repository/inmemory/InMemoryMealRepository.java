package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    protected static final Map<Integer, Map<Integer, Meal>> mealRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        InMemoryUserRepository.userRepository.put(1, new User(1, "userName", "email@mail.ru", "password", Role.USER));
        InMemoryUserRepository.userRepository.put(2, new User(2, "adminName", "email@mail.ru", "password", Role.ADMIN));
        mealRepository.put(1, new ConcurrentHashMap<>());
        mealRepository.put(2, new ConcurrentHashMap<>());
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
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
        if (get(id, userId) != null) {
            return mealRepository.get(userId).remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (mealRepository.containsKey(userId)) {
            if (mealRepository.get(userId).containsKey(id)) {
                return mealRepository.get(userId).get(id);
            }
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return mealRepository.get(userId) == null ? Collections.emptyList() :
                mealRepository.get(userId).values().stream()
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAll(int userId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return mealRepository.get(userId) == null ? Collections.emptyList() :
                mealRepository.get(userId).values().stream()
                        .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate.plusDays(1)) &&
                                DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                        .collect(Collectors.toList());
    }
}

