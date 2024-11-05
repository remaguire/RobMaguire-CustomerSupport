<html>
<head>
    <title>Customer Support</title>
</head>

<body>
    <%--@elvariable id="ticketForm" type="org.example.robmaguirecustomersupport.site.TicketController.TicketForm"--%>
    <form method="post" action="ticket/add" enctype="multipart/form-data" modelAttribute="ticketForm">
        <p><form:label path="subject">Subject</form:label></p>
        <p><form:input path="subject"/></p>
        <p><form:label path="body">Body</form:label>form:label></p>
        <p><form:textarea path="body" rows="5" cols="30"/></p>
        <p><b>Attachments</b></p>>
        <p><input type="file" name="attachments" multiple="multiple"></p>
        <p><input type="submit" value="Submit"></p>>
    </form>
</body>
</html>
