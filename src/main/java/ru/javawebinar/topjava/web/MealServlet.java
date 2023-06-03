package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.ListStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Storage storage = new ListStorage();
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Meal> meals = MealsUtil.init();
        String action = request.getParameter("action") == null ? "default" : request.getParameter("action");
        String id = request.getParameter("id");
        Meal m;
        if (action.equals("default") || action.equals("delete")) {
            if (action.equals("delete")) {
                log.debug(action + " " + id);
                storage.delete(Integer.parseInt(id));
            } else {
                log.debug("redirect to meals");
            }
            request.setAttribute("meal", MealsUtil.filteredByStreams(meals, MealsUtil.CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equals("edit") || action.equals("add")) {
            if (action.equals("edit")) {
                log.debug("edit " + id);
                m = storage.get(Integer.parseInt(id));
                request.setAttribute("action", "edit");
            } else {
                log.debug("create new meal");
                m = new Meal();
                request.setAttribute("action", "add");
            }
            request.setAttribute("meal", m);
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("datetime"));
        Meal meal;
        if (!id.isEmpty()) {
            if (storage.getAll().stream()
                    .anyMatch(m -> m.getId() == Integer.parseInt(id))) {
                storage.update(Integer.parseInt(id), new Meal(dateTime, description, calories));
            }
        } else {
            meal = new Meal(dateTime, description, calories);
            storage.save(meal);
        }
        response.sendRedirect("meals");
    }

}
