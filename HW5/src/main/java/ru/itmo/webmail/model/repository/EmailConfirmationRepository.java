package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.EmailConfirmation;
import ru.itmo.webmail.model.domain.User;

import java.util.List;

public interface EmailConfirmationRepository {
    EmailConfirmation find(long confirmationId);
    EmailConfirmation findByUser(User user);
    EmailConfirmation findBySecret(String secret);
    List<EmailConfirmation> findAll();
    void save(EmailConfirmation confirmation);
    void delete(EmailConfirmation confirmation);
}
