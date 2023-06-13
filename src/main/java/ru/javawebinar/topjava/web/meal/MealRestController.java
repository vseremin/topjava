package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("get all");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("get all filtered");
        startDate = startDate != null ? startDate : LocalDate.MIN;
        endDate = endDate != null ? endDate.plusDays(1) : LocalDate.MAX;
        startTime = startTime != null ? startTime : LocalTime.MIN;
        endTime = endTime != null ? endTime : LocalTime.MAX;
        return MealsUtil.getFilteredTos(service.getAllFiltered(SecurityUtil.authUserId(), startDate, endDate),
                SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public Meal get(int id) {
        Meal meal = service.get(id, SecurityUtil.authUserId());
        log.info("get {}", id);
        return meal;
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        ValidationUtil.checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        service.delete(id, SecurityUtil.authUserId());
        log.info("delete {}", id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }
}