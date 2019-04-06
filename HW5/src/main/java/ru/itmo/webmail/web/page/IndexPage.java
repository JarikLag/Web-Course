package ru.itmo.webmail.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class IndexPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void registrationDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "You have been registered. To verify your registration click the link below");
        view.put("secretConfirmation", request.getSession().getAttribute("secretConfirmation"));
    }

    private void accessDenied(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "Access denied, you are not allowed to view this page");
    }
}
