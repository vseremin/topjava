package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends MealRestController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "/meals";
    }

    @GetMapping("/update/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("meal", super.get(id));
        return "/mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal());
        return "/mealForm";
    }

    @PostMapping({"/update/meals", "/meals"})
    public String edit(HttpServletRequest request) {
        Meal meal = new Meal();
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        String id = request.getParameter("id");
        if (StringUtils.hasText(id)) {
            super.update(meal, Integer.parseInt(id));
        } else {
            super.create(meal);
        }
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filter(Model model, HttpServletRequest request) {
        String paramStartDate = request.getParameter("startDate");
        LocalDate startDate = StringUtils.hasText(paramStartDate) ? LocalDate.parse(paramStartDate) : null;
        String paramEndDate = request.getParameter("endDate");
        LocalDate endDate = StringUtils.hasText(paramEndDate) ? LocalDate.parse(paramEndDate) : null;
        String paramStartTime = request.getParameter("startTime");
        LocalTime startTime = StringUtils.hasText(paramStartTime) ? LocalTime.parse(paramStartTime) : null;
        String paramEndTime = request.getParameter("endTime");
        LocalTime endTime = StringUtils.hasText(paramEndTime) ? LocalTime.parse(paramEndTime) : null;
        log.info("filtered");
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "/meals";
    }
}
