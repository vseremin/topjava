<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://localhost:8080/dateconverter" %>
<html>
<head>
    <title>Meal list</title>
</head>
<body>
<center>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <H1>Meals</H1>
    <a href="meals?action=add">Add Meal</a>
    <p>
    <table border=3>
        <thead>
        <tr>
            <th>Data</th>
            <th>Description</th>
            <th>Calories</th>
            <th colspan=2>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${meals}" var="meal">
            <tr style="color: ${meal.excess ? 'red' : 'green'};">
                <td>${f:formatLocalDateTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=edit&id=${meal.id}"/>Update</td>
                <td><a href="meals?action=delete&id=${meal.id}"/>Delete</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</center>
</body>
</html>
