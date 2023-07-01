package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = "datajpa")
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getUserWithMeals() {
        User testUser = service.getUserWithMeals(USER_ID);
        USER_MATCHER.assertMatch(testUser, user);
        for (int i = 0; i < meals.size(); i++) {
            MEAL_MATCHER.assertMatch(testUser.getMeals().get(i), meals.get(i));
        }
    }
}