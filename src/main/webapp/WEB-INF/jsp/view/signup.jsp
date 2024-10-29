<html>
<head>
    <title>Customer Support</title>
</head>

<body>
    <h2>Sign Up</h2>
    <p>Enter a username and password.</p>
    <%
        if ((Boolean) request.getAttribute("loginFailed")) {
    %>
    <b>Must enter a unique username and a password!</b><br>
    <%
        }
    %>

    <form method="POST" action="<c:url value="/login" />">
        <input type="hidden" name="action" value="signup">
        <p>Username</p>
        <input type="text" name="username" />
        <p>Password</p>
        <input type="password" name="password" />
        <br>
        <input type="submit" value="Sign Up" />
    </form>
</body>
</html>