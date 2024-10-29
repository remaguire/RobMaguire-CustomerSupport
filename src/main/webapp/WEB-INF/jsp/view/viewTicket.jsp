<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--@elvariable id="ticketId" type="java.lang.String"--%>
<%--@elvariable id="ticket" type="org.example.robmaguirecustomersupport.Ticket"--%>
<html>
<head>
    <title>Customer Support</title>
</head>

<body>
    <h2>Ticket #${ticketId}:</h2>
    <p><i>Customer Name: <c:out value="${ticket.customerName}" /></i></p>
    <p>Subject: <c:out value="${ticket.subject}" /></p>
    <p><c:out value="${ticket.ticketBody}"/></p>
    <c:if test="${ticket.numberOfAttachments > 0}">
        Attachments:
        <c:forEach items="${ticket.attachments}" var="attachment" varStatus="status">
        <c:if test="${!status.first}">, </c:if>
            <a href="<c:url value="/tickets">
                <c:param name="action" value="download" />
                <c:param name="ticketId" value="${ticketId}" />
                <c:param name="attachment" value="${attachment.value.name}" />
                </c:url>"><c:out value="${attachment.value.name}" /></a>
        </c:forEach>
    </c:if>
</body>
</html>
