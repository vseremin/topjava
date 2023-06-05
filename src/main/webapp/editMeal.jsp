<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ taglib prefix="f" uri="http://localhost:8080/dateconverter" %>
<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${param.action.equals("edit") ? "Edit Meal" : "Add Meal"}</h2>
<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="id" value="${meal.id}">
    <dl>
        DateTime: <input type="datetime-local" name="datetime" size=50
                         value="${meal.id != null ? meal.dateTime :  f:formatLocalDateTime(LocalDateTime.now())}"
                         required>
    </dl>
    <dl>
        Description: <input type="text" name="description" size=50 value="${meal.description}" required>
    </dl>
    <dl>
        Calories: <input type="number" name="calories" size=50 value="${meal.calories}" required>
    </dl>
    <hr>
    <button type="submit">Сохранить</button>
    <button type="button" onclick="window.history.back()">Отменить</button>
</form>
</body>
</html>
