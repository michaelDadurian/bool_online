<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<!DOCTYPE HTML>
<html>

    <h1>Your Circuits:
        <a href="profile/login" > </a>
    </h1>


    <table>
        <c:forEach items="${circuitNames}" var="circuitName">
            <tr>
                <td>${circuitName}</td>
            </tr>
        </c:forEach>
    </table>

</html>