package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.service.UserService;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class RegisterPage extends Page {
    private void register(HttpServletRequest request, Map<String, Object> view) {
        User user = new User();
        user.setLogin(request.getParameter("login"));
        user.setEmail(request.getParameter("email"));
        user.setConfirmed(false);
        String password = request.getParameter("password");

        try {
            getUserService().validateRegistration(user, password);
        } catch (ValidationException e) {
            view.put("login", user.getLogin());
            view.put("email", user.getEmail());
            view.put("confirmed", user.getConfirmed());
            view.put("password", password);
            view.put("error", e.getMessage());
            return;
        }

        String secret = getEmailConfirmationService().generateSecret(user.getLogin() + user.getEmail());

        getUserService().register(user, password);
        getEmailConfirmationService().addConfirmation(user.getId(), secret);

        request.getSession(true).setAttribute("secretConfirmation", secret);
        throw new RedirectException("/index", "registrationDone");
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }
}
