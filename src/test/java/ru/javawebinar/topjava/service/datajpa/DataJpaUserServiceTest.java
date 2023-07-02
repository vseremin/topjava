package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getUserWithMeals() {
        User testUser = service.getWithMeals(USER_ID);
        MEAL_MATCHER.assertMatch(testUser.getMeals(), meals);
        USER_MATCHER.assertMatch(testUser, user);
    }

    @Test
    public void getUserWithoutMeals() {
        User testUser = service.getWithMeals(GUEST_ID);
        MEAL_MATCHER.assertMatch(testUser.getMeals(), Collections.EMPTY_LIST);
    }
}