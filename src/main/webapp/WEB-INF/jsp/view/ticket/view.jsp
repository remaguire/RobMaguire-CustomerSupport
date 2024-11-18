<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--@elvariable id="ticketId" type="java.lang.String"--%>
<%--@elvariable id="ticket" type="org.example.robmaguirecustomersupport.site.Ticket"--%>
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
        <p>Attachments:</p>
        <c:forEach items="${ticket.attachments}" var="attachment" varStatus="status">
        <c:if test="${!status.first}">, </c:if>
            <a href="<c:url value="/ticket/${ticketId}/attachment/${attachment.value.name}">
                </c:url>"><c:out value="${attachment.value.name}" /></a>
        </c:forEach>
        <p />
    </c:if>
</body>
</html>
