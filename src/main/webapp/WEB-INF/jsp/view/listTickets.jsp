<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--@elvariable id="tickets" type="java.util.Map<Integer, org.example.robmaguirecustomersupport.Ticket>"--%>
<html>
<head>
    <title>List Tickets</title>
</head>

<body>
    <h2>Tickets</h2>
    <c:choose>
        <c:when test="${tickets.size() == 0}">
            <p>No tickets have been created.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="entry" items="${tickets}">
                <p>Ticket #${entry.key}: <a href="<c:url value="/tickets">
                    <c:param name="action" value="view" />
                    <c:param name="ticketId" value="${entry.key}" /></c:url>">
                    ${entry.value.subject}</a></p>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</body>
</html>
