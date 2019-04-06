package ru.itmo.webmail.model.service;

import com.google.common.hash.Hashing;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class UserService {
    private static final String USER_PASSWORD_SALT = "dc3475f2b301851b";

    private UserRepository userRepository = new UserRepositoryImpl();

    private void validateLogin(String login) throws ValidationException {
        if (login == null || login.isEmpty()) {
            throw new ValidationException("Login is required");
        }
        if (!login.matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (login.length() > 8) {
            throw new ValidationException("Login can't be longer than 8");
        }
    }

    private void validateEmail(String email) throws ValidationException {
        if (email == null ||email.isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (!email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
            throw new ValidationException("Incorrect e-mail");
        }
    }

    private void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4");
        }
        if (password.length() > 32) {
            throw new ValidationException("Password can't be longer than 32");
        }
    }

    public void validateRegistration(User user, String password) throws ValidationException {
        validateLogin(user.getLogin());
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new ValidationException("Login is already in use");
        }
        validateEmail(user.getEmail());
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("E-mail is already in use");
        }
        validatePassword(password);
    }

    public void register(User user, String password) {
        String passwordSha = getPasswordSha(password);
        userRepository.save(user, passwordSha);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void validateEnter(String login, String password) throws ValidationException {
        try {
            validateLogin(login);
        } catch (ValidationException e) {
            validateEmail(login);
        }
        validatePassword(password);
        if (userRepository.findByLoginOrEmailAndPasswordSha(login, getPasswordSha(password)) == null) {
            throw new ValidationException("Invalid login or password");
        }
    }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashString(USER_PASSWORD_SALT + password,
                StandardCharsets.UTF_8).toString();
    }

    public User authorize(String login, String password) {
        return userRepository.findByLoginOrEmailAndPasswordSha(login, getPasswordSha(password));
    }

    public User find(long userId) {
        return userRepository.find(userId);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void updateConfirmation(long id) {
        userRepository.updateConfirmation(id);
    }
}
