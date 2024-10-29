<html>
<head>
    <title>Customer Support</title>
</head>

<body>
    <h2>Log In</h2>
    <p>You must log in to edit or view tickets.</p>
    <%
        if ((Boolean) request.getAttribute("loginFailed")) {
    %>
    <b>Entered username or password was incorrect.</b><br>
    <%
        }
    %>

    <form method="POST" action="<c:url value="/login" />">
        <p>Username</p>
        <input type="text" name="username" />
        <p>Password</p>
        <input type="password" name="password" />
        <br>
        <input type="submit" value="Log In" />
    </form>
</body>
</html>