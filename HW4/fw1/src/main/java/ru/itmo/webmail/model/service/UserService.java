package ru.itmo.webmail.model.service;

import com.google.common.hash.Hashing;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserService {
    private static final String USER_PASSWORD_SALT = "dc3475f2b301851b";

    private UserRepository userRepository = new UserRepositoryImpl();

    public void validateRegistration(User user, String password, String passwordConfirmation) throws ValidationException {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Login is required");
        }
        if (!user.getLogin().matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (user.getLogin().length() > 8) {
            throw new ValidationException("Login can't be longer than 8");
        }
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new ValidationException("Login is already in use");
        }

        if (!user.getEmail().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
            throw new ValidationException("Incorrect e-mail");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("E-mail is already in use");
        }

        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4");
        }
        if (password.length() > 32) {
            throw new ValidationException("Password can't be longer than 32");
        }
        if (!passwordConfirmation.equals(password)) {
            throw new ValidationException("Password and confirmation field are different");
        }
    }

    public User validateLogin(String login, String password) throws ValidationException {
        User user = userRepository.findByLogin(login);
        String passwordHash = Hashing.sha256().hashString(USER_PASSWORD_SALT + password,
                StandardCharsets.UTF_8).toString();

        if (user == null || !passwordHash.equals(user.getPasswordSha1())) {
            throw new ValidationException("Incorrect username or password");
        }

        return user;
    }

    public void register(User user, String password) {
        user.setPasswordSha1(Hashing.sha256().hashString(USER_PASSWORD_SALT + password,
                StandardCharsets.UTF_8).toString());
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public long findCount() {
        return userRepository.findCount();
    }
}
