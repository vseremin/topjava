package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = "datajpa")
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
        USER_MATCHER.assertMatch(actual.getUser(), admin);
    }
}
