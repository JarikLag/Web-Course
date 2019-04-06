package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.NewsRepository;
import ru.itmo.webmail.model.repository.impl.NewsRepositoryImpl;
import ru.itmo.webmail.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Page {
    private UserService userService = new UserService();
    private NewsRepository newsRepository = new NewsRepositoryImpl();

    void before(HttpServletRequest request, Map<String, Object> view) {
        User user = (User)request.getSession().getAttribute("user");
        view.put("username", user == null ? null : user.getLogin());

        view.put("newsList", newsRepository.findAll());
    }

    void after(HttpServletRequest request, Map<String, Object> view) {
        view.put("userCount", userService.findCount());
    }
}
