<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<!DOCTYPE HTML>
<html>

    <h1><c:out value="${currUser}"/> Circuits:
        <a href="profile"> </a>
    </h1>

    <a href = "profile/all">All Circuits</a>
    <a href = "profile/profile">All Circuits</a>
    <a href = "profile/shared">All Circuits</a>
    <a href = "profile/public">All Circuits</a>

    <table>
        <c:forEach items="${circuitNames}" var="circuitName">
            <tr>
                <td>${circuitName}</td>
            </tr>
        </c:forEach>
    </table>

</html>