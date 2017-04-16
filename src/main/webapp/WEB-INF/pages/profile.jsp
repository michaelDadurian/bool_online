<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>

    <table>
        <c:forEach items="${Strings}" var="astring">
            <tr>
                <td>${astring}</td>
            </tr>
        </c:forEach>
    </table>

</html>