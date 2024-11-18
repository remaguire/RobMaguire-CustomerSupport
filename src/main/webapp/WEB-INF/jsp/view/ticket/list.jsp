<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--@elvariable id="tickets" type="java.util.Map<Integer, org.example.robmaguirecustomersupport.site.Ticket>"--%>
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
            <c:forEach var="ticket" items="${tickets}">
                <p>Ticket #${ticket.ticketId}: <a href="<c:url value="/ticket/view/${ticket.ticketId}"/>">
                        ${ticket.subject}</a></p>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</body>
</html>
