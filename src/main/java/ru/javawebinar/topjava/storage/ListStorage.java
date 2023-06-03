package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ListStorage implements Storage {

    private static final List<Meal> storage = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicInteger id = new AtomicInteger();
//    private static int id = 0;
    //    private static final Object LOCK = new Object();

    @Override
    public void update(int id, Meal meal) {
        Meal updatedMeal = get(id);
        if (updatedMeal != null) {
            updatedMeal.setDescription(meal.getDescription());
            updatedMeal.setCalories(meal.getCalories());
            updatedMeal.setDateTime(meal.getDateTime());
        }
    }

    @Override
    public void save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(generatedId());
        }
        storage.add(meal);
    }

    @Override
    public Meal get(int id) {
        return storage.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(int id) {
        storage.stream()
                .filter(m -> m.getId() == id)
                .findFirst().ifPresent(storage::remove);
    }

    @Override
    public List<Meal> getAll() {
        return storage;
    }

    public static int generatedId() {
//        synchronized (LOCK) {
//            id++;
//        }
//        return id;
        return id.incrementAndGet();
    }
}
