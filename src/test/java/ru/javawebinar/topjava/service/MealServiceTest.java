package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(MEAL_ID, USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void delete() {
        mealService.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> mealsBetweenDate = mealService.getBetweenInclusive(LocalDate.of(2023, Month.JANUARY, 30),
                LocalDate.of(2023, Month.JANUARY, 30), USER_ID);
        assertMatch(mealsBetweenDate, Arrays.asList(meal3, meal2, meal1));
    }

    @Test
    public void getBetweenNullDate() {
        List<Meal> mealsBetweenDate = mealService.getBetweenInclusive(null, null, USER_ID);
        assertMatch(mealsBetweenDate, Arrays.asList(meal7, meal6, meal5, meal4, meal3, meal2, meal1));
    }

    @Test
    public void getAll() {
        List<Meal> all = mealService.getAll(USER_ID);
        assertMatch(all, Arrays.asList(meal7, meal6, meal5, meal4, meal3, meal2, meal1));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(updated.getId(), USER_ID), getUpdated());
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.update(meal1, ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DuplicateKeyException.class, () -> mealService.create(new Meal(null,
                LocalDateTime.of(2023, Month.JANUARY, 30, 10, 0), "Duplicate", 1000), USER_ID));
    }

    @Test
    public void createDateMaxVal() {
        createWithDate(LocalDateTime.MAX);
    }

    @Test
    public void createDateMinVal() {
        createWithDate(LocalDateTime.MIN);
    }

    @Test
    public void createDateNull() {
        assertThrows(DataIntegrityViolationException.class, () -> createWithDate(null));
    }

    @Test
    public void updateDateNull() {
        Meal updated = getUpdated();
        updated.setDateTime(null);
        assertThrows(DataIntegrityViolationException.class, () -> mealService.update(updated, USER_ID));
    }

    private void createWithDate(LocalDateTime ldt) throws DataIntegrityViolationException {
        Meal created = mealService.create(getNew(ldt), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew(ldt);
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, USER_ID), newMeal);
    }
}