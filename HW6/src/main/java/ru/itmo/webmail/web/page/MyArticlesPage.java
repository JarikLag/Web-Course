package ru.itmo.webmail.web.page;

import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyArticlesPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private Map<String, Object> update(HttpServletRequest request, Map<String, Object> view) {
        Long id = Long.parseLong(request.getParameter("id"));
        Long userId = Long.parseLong(request.getParameter("userId"));
        if (getUser().getId() == userId) {
            boolean update = Boolean.parseBoolean(request.getParameter("update"));
            getArticleService().updateVisibility(id, update);
            view.put("success", true);
        } else {
            view.put("success", false);
        }
        return view;
    }

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (getUser() == null) {
            throw new RedirectException("/index");
        }
        view.put("articles", getArticleService().findByUser(getUser().getId()));
    }
}
