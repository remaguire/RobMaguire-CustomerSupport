package org.example.robmaguirecustomersupport.site;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("ticket")
public class TicketController {
    private volatile int TICKET_ID_SEQUENCE = 1;
    private final Map<Integer, Ticket> tickets = new HashMap<>();

    public record TicketForm(String subject, String body, List<MultipartFile> attachments) {
    }

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(Map<String, Object> model) {
        model.put("tickets", tickets);
        return "ticket/list";
    }

    @RequestMapping(value = "view/{ticketId}", method = RequestMethod.GET)
    public ModelAndView view(Map<String, Object> model, @PathVariable("ticketId") int ticketId) {
        final var ticket = tickets.get(ticketId);
        if (ticket == null) return getListRedirectModelAndView();

        model.put("ticketId", Integer.toString(ticketId));
        model.put("ticket", ticket);
        return new ModelAndView("ticket/view");
    }

    @RequestMapping(value = "/{ticketId}/attachment/{attachment:.+}", method = RequestMethod.GET)
    public View download(@PathVariable("ticketId") int ticketId, @PathVariable("attachment") String name) {
        final var ticket = tickets.get(ticketId);
        if (ticket == null) return getListRedirectView();

        final var attachment = ticket.getAttachment(name);
        if (attachment == null) {
            return getListRedirectView();
        }

        return new DownloadingView(attachment.getName(), attachment.getContents());
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(Map<String, Object> model) {
        model.put("ticketForm", new TicketForm(null, null, null));
        return "ticket/add";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(HttpSession session, TicketForm form) throws IOException {
        final var ticket = new Ticket();
        ticket.setCustomerName((String) session.getAttribute("username"));
        ticket.setSubject(form.subject());
        ticket.setTicketBody(form.body());

        for (MultipartFile part : form.attachments()) {
            final var attachment = new Attachment();
            attachment.setName(part.getOriginalFilename());
            attachment.setContents(part.getBytes());
            if (attachment.getName() != null && attachment.getContents().length > 0)
                ticket.addAttachment(attachment);
        }

        final int ticketId;
        synchronized (this) {
            ticketId = TICKET_ID_SEQUENCE++;
        }

        tickets.put(ticketId, ticket);
        return new RedirectView("/ticket/view/" + ticketId, true, false);
    }

    private ModelAndView getListRedirectModelAndView() {
        return new ModelAndView(getListRedirectView());
    }

    private View getListRedirectView() {
        return new RedirectView("/ticket/list", true, false);
    }
}