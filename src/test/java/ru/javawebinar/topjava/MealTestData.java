package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL_ID = START_SEQ + 3;
    public static final Meal meal1 = new Meal(MEAL_ID, LocalDateTime.of(2023, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal meal2 = new Meal(MEAL_ID + 1, LocalDateTime.of(2023, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal meal3 = new Meal(MEAL_ID + 2, LocalDateTime.of(2023, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal meal4 = new Meal(MEAL_ID + 3, LocalDateTime.of(2023, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal meal5 = new Meal(MEAL_ID + 4, LocalDateTime.of(2023, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal meal6 = new Meal(MEAL_ID + 5, LocalDateTime.of(2023, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal meal7 = new Meal(MEAL_ID + 6, LocalDateTime.of(2023, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final Meal adminMeal = new Meal(MEAL_ID + 7, LocalDateTime.of(2023, Month.JANUARY, 30, 10, 0), "Завтрак", 500);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2023, Month.JUNE, 30, 0, 0), "New meal", 500);
    }

    public static Meal getNew(LocalDateTime ldt) {
        return new Meal(ldt, "New meal", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDateTime(LocalDateTime.of(2023, Month.APRIL, 4, 8, 0));
        updated.setDescription("Updated");
        updated.setCalories(10000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
