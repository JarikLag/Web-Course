package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.service.UserService;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;

public class LogoutPage extends Page {
    private UserService userService = new UserService();

    private void action(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        throw new RedirectException("/index", "logoutDone");
    }
}
