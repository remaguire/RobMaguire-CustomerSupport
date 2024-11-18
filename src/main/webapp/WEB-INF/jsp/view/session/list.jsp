<%@ page import="java.util.List" %>
<%@ page import="org.example.robmaguirecustomersupport.entities.UserPrincipal" %>
<%!
    private static String toString(long timeInterval) {
        if (timeInterval < 1_000) return "less than one second";
        if (timeInterval < 60_000) return (timeInterval / 1_000) + " seconds";
        return "about " + (timeInterval / 60_000) + " minutes";
    }
%>
<%
    int numberOfSessions = (Integer) request.getAttribute("numberOfSessions");
    @SuppressWarnings("unchecked")
    List<HttpSession> sessions = (List<HttpSession>) request.getAttribute("sessionList");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>

    <body>
        <h2>Sessions</h2>
        <p>There are <%= numberOfSessions %> active sessions.</p>
        <%
            long timestamp = System.currentTimeMillis();
            for (HttpSession ses : sessions) {
                final UserPrincipal principal = (UserPrincipal) UserPrincipal.getPrincipal(ses);
                if (principal == null)
                    out.print("<p>" + ses.getId() + " - not logged in");
                else
                    out.print("<p>" + ses.getId() + " - " + principal.getUsername());
                if (ses.getId().equals(session.getId()))
                    out.print(" (you)");
                out.print(" - last active " + toString(timestamp - ses.getLastAccessedTime()));
                out.println(" ago</p>");
            }
        %>
    </body>
</html>