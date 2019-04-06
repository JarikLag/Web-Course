package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.EmailConfirmation;
import ru.itmo.webmail.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ConfirmPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        EmailConfirmation confirmation = getEmailConfirmationService().findBySecret(request.getParameter("secret"));
        if (confirmation != null) {
            getUserService().updateConfirmation(confirmation.getUserId());
            getEmailConfirmationService().deleteConfitmation(confirmation);
            view.put("message", "Confirmation successful");
        }
        else {
            view.put("message", "Where is nothing to confirm");
        }
    }
}
