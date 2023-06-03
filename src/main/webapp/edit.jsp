<%--
  Created by IntelliJ IDEA.
  User: vyacheslav
  Date: 03.06.2023
  Time: 00:06
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>

<c:choose>
    <c:when test="${action == 'edit'}"><h2>Edit Meal</h2></c:when>
    <c:when test="${action == 'add'}"><h2>Add Meal</h2></c:when>
</c:choose>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            DateTime: <input type="datetime-local" name="datetime" size=50 value="${meal.dateTime}" required>
        </dl>
        <dl>
            Description: <input type="text" name="description" size=50 value="${meal.description}" required>
        </dl>
        <dl>
            Calories: <input type="text" name="calories" size=50 value="${meal.calories}" required>
        </dl>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="button" onclick="window.history.back()">Отменить</button>
    </form>
</body>
</html>
