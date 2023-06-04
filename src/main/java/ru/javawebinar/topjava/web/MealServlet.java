package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MapStorageMeal;
import ru.javawebinar.topjava.storage.StorageMeal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final StorageMeal storage = new MapStorageMeal();
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "default" : request.getParameter("action");
        String id = request.getParameter("id");
        Object m;
        String path;
        switch (action) {
            case "edit": {
                m = storage.get(Integer.parseInt(id));
                if(m != null) {
                    path = "/editmeal.jsp";
                } else {
                    log.debug("meal with id {} does not exist", id);
                    response.sendRedirect("meals");
                    return;
                }
                log.debug("edit meal with id {}", id);
            }
            break;
            case "add": {
                m = new Meal();
                path = "/editmeal.jsp";
                log.debug("create new meal");
            }
            break;
            case "delete": {
                log.debug("delete meal with id {}", id);
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                return;
            }
            default: {
                m = MealsUtil.filteredByStreams(storage.getAll(), MealsUtil.CALORIES_PER_DAY);
                log.debug("redirect to meals");
                path = "meals.jsp";
            }
        }
        request.setAttribute("meal", m);
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
