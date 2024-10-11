<%@ page session="false" %>
<!DOCTYPE html>
<head>
    <title>Customer Support</title>
</head>

<body>
    <h2>Create a Ticket</h2>
    <br>
    <form method="post" action="tickets" enctype="multipart/form-data">
        <input type="hidden" name="action" value="create">
        <p>Your Name</p>
        <input type="text" name="customerName">
        <p>Subject</p>
        <input type="text" name="subject">
        <p>Body</p>>
        <textarea name="body" rows="5" cols="30"></textarea>
        <p><b>Attachments</b></p>>
        <input type="file" name="file1">
        <p><input type="submit" value="Submit"></p>>
    </form>
</body>
</html>
