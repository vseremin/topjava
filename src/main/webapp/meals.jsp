<%--
  Created by IntelliJ IDEA.
  User: vyacheslav
  Date: 02.06.2023
  Time: 20:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://localhost:8080/dateconverter"%>
<html>
<head>
    <title>Meal list</title>
</head>
<body>
<center>
<H1>Meals</H1>
<a href="meals?action=add">Add Meal</a>
    <p>
    <table border = 3>
        <thead>
            <tr>
                <th>Id</th>
                <th>Data</th>
                <th>Description</th>
                <th>Calories</th>
                <th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${meal}" var="meal">
            <tr style="${meal.excess ? 'color:red;' : 'color:green;'}">
                <td><c:out value="${meal.id}"/></td>
                <td><c:out value="${f:formatLocalDateTime(meal.dateTime, 'yyyy-MM-dd HH:mm')}"/></td>
                <td><c:out value="${meal.description}"/></td>
                <td><c:out value="${meal.calories}"/></td>
                <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a> </td>
                <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a> </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</center>
</body>
</html>
