package ru.itmo.webmail.web.page;

import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AdminPage extends Page {
    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (getUser() == null || !getUser().isAdmin()) {
            throw new RedirectException("/index");
        }
        view.put("users", getUserService().findAll());
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private Map<String, Object> update(HttpServletRequest request, Map<String, Object> view) {
        if (getUser().isAdmin()) {
            Long id = Long.parseLong(request.getParameter("userId"));
            Boolean update = Boolean.parseBoolean(request.getParameter("update"));
            getUserService().updatePrivileges(id, update);
            view.put("success", true);
        }
        else {
            view.put("success", false);
        }
        return view;
    }
}
