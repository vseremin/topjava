package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT u FROM User u WHERE u.id=:id")
    User findUser(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Query(name = Meal.ALL_SORTED)
    List<Meal> getAll(@Param("userId") int userId);

    @Query(name = Meal.GET_BETWEEN)
    List<Meal> getBetween(@Param("userId") int userId, @Param("startDateTime")LocalDateTime startDateTime,
                          @Param("endDateTime")LocalDateTime endDateTime);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:userId")
    Meal getWithUser(@Param("id") int id, @Param("userId") int userId);
}
