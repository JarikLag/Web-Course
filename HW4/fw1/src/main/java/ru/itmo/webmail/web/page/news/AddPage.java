package ru.itmo.webmail.web.page.news;

import ru.itmo.webmail.model.domain.News;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.NewsRepository;
import ru.itmo.webmail.model.repository.impl.NewsRepositoryImpl;
import ru.itmo.webmail.web.exception.RedirectException;
import ru.itmo.webmail.web.page.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AddPage extends Page {
    private NewsRepository newsRepository = new NewsRepositoryImpl();

    private void add(HttpServletRequest request, Map<String, Object> view) {
        String news = request.getParameter("news");
        User user = (User)request.getSession().getAttribute("user");
        if (user != null) {
            newsRepository.save(new News(user.getId(), news));
        } else {
            view.put("error", "Only authorized users can submit news");
            return;
        }

        throw new RedirectException("/index");
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }
}
