package org.example.robmaguirecustomersupport;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(
        name = "ticketServlet",
        urlPatterns = {"/tickets"},
        loadOnStartup = 1
)
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 5, // 5 MB
        maxFileSize = 1024 * 1024 * 20, // 20 MB
        maxRequestSize = 1024 * 1024 * 40 // 40 MB
)
public class TicketServlet extends HttpServlet {
    private volatile int TICKET_ID_SEQUENCE = 1;
    private final Map<Integer, Ticket> tickets = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final String action = Optional.ofNullable(request.getParameter("action")).orElse("list");
        switch (action) {
            case "create":
                this.showTicketForm(request, response);
                break;
            case "view":
                this.viewTicket(request, response);
                break;
            case "download":
                this.downloadAttachment(request, response);
                break;
            case "list":
            default:
                this.listTickets(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final String action = Optional.ofNullable(request.getParameter("action")).orElse("list");
        switch (action) {
            case "create":
                this.createTicket(request, response);
                break;
            case "list":
            default:
                response.sendRedirect("tickets");
        }
    }

    private void listTickets(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("tickets", tickets);
        request.getRequestDispatcher("/WEB-INF/jsp/view/listTickets.jsp").forward(request, response);
    }

    private void viewTicket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String ticketId = request.getParameter("ticketId");
        final Ticket ticket = this.getTicket(ticketId, response);
        if (ticket == null) return;

        final PrintWriter writer = response.getWriter();
        writer.append(String.format("""
                        <h2>Ticket #%s: %s</h2><br>
                        <i>Customer Name - %s</i><br>
                        <br>
                        %s<br>
                        <br>""",
                ticketId,
                ticket.getSubject(),
                ticket.getCustomerName(),
                ticket.getTicketBody()));

        final Map<String, Attachment> attachmentMap = ticket.getAttachments();
        if (!attachmentMap.isEmpty()) {
            writer.append("Attachments: ");
            for (Map.Entry<String, Attachment> attachmentEntry : attachmentMap.entrySet()) {
                final String attachmentId = attachmentEntry.getKey();
                final Attachment attachment = attachmentEntry.getValue();
                writer.append(String.format("""
                                <a href="tickets?action=download&ticketId=%s&attachment=%s>%s</a>""",
                        attachmentId,
                        attachment.getName(),
                        attachment.getName()));
            }
            writer.append("<br><br>");
        }

        writer.append("""
                <a href="tickets">Return to list tickets</a><br>""");
    }

    private void createTicket(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final Ticket ticket = new Ticket();
        ticket.setCustomerName(request.getParameter("customerName"));
        ticket.setSubject(request.getParameter("subject"));
        ticket.setTicketBody(request.getParameter("body"));

        final Part filePart = request.getPart("file1");
        if (filePart != null && filePart.getSize() > 0) {
            final Attachment attachment = this.processAttachment(filePart);
            if (attachment != null) ticket.addAttachment(attachment);
        }

        final int ticketId;
        synchronized (this) {
            ticketId = this.TICKET_ID_SEQUENCE++;
            this.tickets.put(ticketId, ticket);
        }

        response.sendRedirect("tickets?action=view&ticketId=" + ticketId);
    }

    private void downloadAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String ticketId = request.getParameter("ticketId");
        final Ticket ticket = this.getTicket(ticketId, response);
        if (ticket == null) return;

        final String attachmentName = request.getParameter("attachment");
        if (attachmentName == null) {
            response.sendRedirect("tickets?action=view&ticketId=" + ticketId);
            return;
        }

        final Attachment attachment = ticket.getAttachment(attachmentName);
        if (attachment == null) {
            response.sendRedirect("tickets?action=view&ticketId=" + ticketId);
            return;
        }

        response.setHeader("Content-Disposition",
                "attachment; filename=" + attachment.getName());
        response.setContentType("application/octet-stream");
        ServletOutputStream stream = response.getOutputStream();
        stream.write(attachment.getContents());
    }

    private void showTicketForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/jsp/view/ticketForm.jsp").forward(request, response);
    }

    private Attachment processAttachment(Part filePart) throws IOException {
        final InputStream inputStream = filePart.getInputStream();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int read;
        final byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

        final Attachment attachment = new Attachment();
        attachment.setName(filePart.getSubmittedFileName());
        attachment.setContents(outputStream.toByteArray());

        return attachment;
    }

    private Ticket getTicket(String ticketId, HttpServletResponse response) throws IOException {
        if (ticketId == null || ticketId.isEmpty()) {
            response.sendRedirect("tickets");
            return null;
        }

        try {
            final Ticket ticket = this.tickets.get(Integer.parseInt(ticketId));
            if (ticket == null) {
                response.sendRedirect("tickets");
                return null;
            }

            return ticket;
        } catch (NumberFormatException ex) {
            response.sendRedirect("tickets");
            return null;
        }
    }
}