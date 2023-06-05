package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.CollectionMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealStorage storage;

    @Override
    public void init() throws ServletException {
        storage = new CollectionMealStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = Optional.ofNullable(request.getParameter("action")).orElse("default");
        String id = request.getParameter("id");
        String path;
        switch (action) {
            case "edit": {
                Meal meal = storage.get(Integer.parseInt(id));
                if (meal != null) {
                    path = "/editMeal.jsp";
                } else {
                    log.debug("meal with id {} does not exist", id);
                    response.sendRedirect("meals");
                    return;
                }
                log.debug("edit meal with id {}", id);
                request.setAttribute("meal", meal);
            }
            break;
            case "add": {
                path = "/editMeal.jsp";
                log.debug("create new meal");
                request.setAttribute("meal", new Meal());
            }
            break;
            case "delete": {
                log.debug("delete meal with id {}", id);
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                return;
            }
            default: {
                request.setAttribute("meals", MealsUtil.filteredByStreams(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
                log.debug("redirect to meals");
                path = "meals.jsp";
            }
        }

        request.getRequestDispatcher(path).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("datetime"));
        String message;
        Meal meal = new Meal(dateTime, description, calories);
        if (!id.isEmpty()) {
            meal.setId(Integer.parseInt(id));
            message = storage.update(meal) != null ?
                    "meal with id {} has been updated" : "meal with id {} update error";
            log.debug(message, id);
        } else {
            message = storage.create(meal) != null ? "new meal has been created" : "meal creation error";
            log.debug(message);
        }
        response.sendRedirect("meals");
    }

}
