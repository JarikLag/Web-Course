package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage extends Page {
    private Map<String, Object> add(HttpServletRequest request, Map<String, Object> view) {
        Article article = new Article();
        article.setUserId(getUser().getId());
        article.setTitle(request.getParameter("title"));
        article.setText(request.getParameter("articleText"));
        try {
            getArticleService().validate(article);
        } catch (ValidationException e) {
            view.put("error", e.getMessage());
            view.put("success", false);
            return view;
        }
        getArticleService().add(article);
        view.put("success", true);
        return view;
    }

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (getUser() == null) {
            throw new RedirectException("/index");
        }
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }
}
