package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexPage extends Page {
    private class ArticleDisplay {
        Article article;
        String user;
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void registrationDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "You have been registered");
    }

    private List<ArticleDisplay> find(HttpServletRequest request, Map<String, Object> view) {
        List<Article> articles = getArticleService().findAll();
        List<ArticleDisplay> articleDisplays = new ArrayList<>();
        for (Article art : articles) {
            ArticleDisplay artDisp = new ArticleDisplay();
            artDisp.article = art;
            artDisp.user = getUserService().find(art.getUserId()).getLogin();
            articleDisplays.add(artDisp);
        }
        return articleDisplays;
    }
}
