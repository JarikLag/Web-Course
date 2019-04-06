package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Article;

import java.util.List;

public interface ArticleRepository {
    Article find(long articleId);
    List<Article> findByUser(long userId);
    List<Article> findAll();
    void save(Article article);
    void updateVisibility(long articleId, boolean hide);
}
