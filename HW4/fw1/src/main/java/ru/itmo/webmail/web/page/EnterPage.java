package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.service.UserService;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class EnterPage extends Page {
    private UserService userService = new UserService();

    private void login(HttpServletRequest request, Map<String, Object> view) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try {
            User user = userService.validateLogin(login, password);
            request.getSession().setAttribute("user", user);
        } catch (ValidationException e) {
            view.put("login", login);
            view.put("password", password);
            view.put("error", e.getMessage());
            return;
        }

        throw new RedirectException("/index", "loginDone");
    }

    private void action() {
        // No operations.
    }
}
