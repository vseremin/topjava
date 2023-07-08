package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.RootController;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class JspMealController extends MealRestController {
    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private MealService service;

    @Autowired
    private UserService userService;

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("/meals")
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "/meals";
    }

    @GetMapping(path="/meals", params = {"action=update", "id"})
    public String get(HttpServletRequest request, Model model) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        model.addAttribute("id", id);
        model.addAttribute("meal", super.get(id));
        return "forward:/WEB-INF/jsp/mealForm.jsp";
    }

    @GetMapping(path="/meals", params = {"action=delete", "id"})
    public String delete(HttpServletRequest request) {
        super.delete(Integer.parseInt(request.getParameter("id")));
        return "redirect:/meals";
    }

    @GetMapping(path="/meals", params = {"action=create"})
    public String create(Model model) {
        model.addAttribute("meal", new Meal());
        return "forward:/WEB-INF/jsp/mealForm.jsp";
    }

    @PostMapping(value = "/meals")
    public String edit(HttpServletRequest request) {
        Meal meal = new Meal();
        String id = request.getParameter("id");
        Integer userId = SecurityUtil.authUserId();
        if (!StringUtils.isEmpty(id)) {
            meal.setId(Integer.parseInt(id));
            log.info("update meal {} for user {}", id, userId);
        } else {
            log.info("create {} for user {}", id, userId);
        }
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setUser(userService.get(userId));
        service.update(meal, userId);
        return "redirect:/meals";
    }

    @GetMapping(value = "/meals", params = {"action=filter"})
    public String filter(Model model, HttpServletRequest request) {
        LocalDate startDate = null;
        if (!StringUtils.isEmpty(request.getParameter("startDate")))
            startDate = LocalDate.parse(request.getParameter("startDate"));
        LocalDate endDate = null;
        if (!StringUtils.isEmpty(request.getParameter("endDate")))
            endDate = LocalDate.parse(request.getParameter("endDate"));
        LocalTime startTime = null;
        if (!StringUtils.isEmpty(request.getParameter("startTime")))
            startTime = LocalTime.parse(request.getParameter("startTime"));
        LocalTime endTime = null;
        if (!StringUtils.isEmpty(request.getParameter("endTime")))
            endTime = LocalTime.parse(request.getParameter("endTime"));
        log.info("filtered");
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "/meals";
    }
}
