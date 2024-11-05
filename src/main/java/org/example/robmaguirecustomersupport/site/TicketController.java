package org.example.robmaguirecustomersupport.site;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    public static class TicketForm {
        private String subject;
        private String body;
        private List<MultipartFile> attachments;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }
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

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Map<String, Object> model) {
        model.put("ticketForm", new TicketForm());
        return "ticket/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public View add(HttpSession session, @ModelAttribute("ticketForm") TicketForm form) throws IOException {
        final var ticket = new Ticket();
        ticket.setCustomerName((String) session.getAttribute("username"));
        ticket.setSubject(form.getSubject());
        ticket.setTicketBody(form.getBody());

        for (MultipartFile part : form.getAttachments()) {
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