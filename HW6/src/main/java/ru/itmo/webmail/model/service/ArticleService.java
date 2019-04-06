package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.ArticleRepository;
import ru.itmo.webmail.model.repository.impl.ArticleRepositoryImpl;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ArticleService {
    private ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findByUser(long userId) {
        return articleRepository.findByUser(userId);
    }

    public Article find(long articleId){
        return articleRepository.find(articleId);
    }

    public void validate(Article article) throws ValidationException {
        if (article.getTitle() == null || article.getTitle().isEmpty()) {
            throw new ValidationException("Title is empty");
        }
        if (article.getText() == null || article.getText().isEmpty()) {
            throw new ValidationException("Text is empty");
        }
    }

    public void add(Article article) {
        articleRepository.save(article);
    }

    public void updateVisibility(long articleId, boolean hide) {
        articleRepository.updateVisibility(articleId, hide);
    }
}
