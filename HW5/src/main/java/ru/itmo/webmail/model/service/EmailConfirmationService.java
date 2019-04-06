package ru.itmo.webmail.model.service;

import com.google.common.hash.Hashing;
import ru.itmo.webmail.model.domain.EmailConfirmation;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.EmailConfirmationRepository;
import ru.itmo.webmail.model.repository.impl.EmailConfirmationRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class EmailConfirmationService {
    private EmailConfirmationRepository emailConfirmationRepository = new EmailConfirmationRepositoryImpl();

    public void addConfirmation(long userId, String secret) {
        EmailConfirmation confirmation = new EmailConfirmation();
        confirmation.setUserId(userId);
        confirmation.setSecret(secret);
        emailConfirmationRepository.save(confirmation);
    }

    public void deleteConfitmation(EmailConfirmation confirmation) {
        emailConfirmationRepository.delete(confirmation);
    }

    public String generateSecret(String seed) {
        return Hashing.sha256().hashString(seed, StandardCharsets.UTF_8).toString();
    }

    public void validateEmailConfirmation(User user) throws ValidationException {
        if (!user.getConfirmed()) {
            throw new ValidationException("Email is not confirmed.");
        }
    }

    public EmailConfirmation find(long confirmationId) {
        return emailConfirmationRepository.find(confirmationId);
    }

    public EmailConfirmation findBySecret(String secret) {
        return emailConfirmationRepository.findBySecret(secret);
    }

    public List<EmailConfirmation> findAll() {
        return emailConfirmationRepository.findAll();
    }

    public EmailConfirmation findByUser(User user) {
        return emailConfirmationRepository.findByUser(user);
    }
}
