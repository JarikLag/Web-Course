package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.EmailConfirmation;
import ru.itmo.webmail.model.domain.Event;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.repository.EmailConfirmationRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailConfirmationRepositoryImpl implements EmailConfirmationRepository {
    private static final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public EmailConfirmation find(long confirmationId) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM EmailConfirmation WHERE id=?")) {
                statement.setLong(1, confirmationId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return toEmailConfirmation(statement.getMetaData(), resultSet);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find EmailConfirmation by id.", e);
        }
    }

    @Override
    public EmailConfirmation findByUser(User user) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM EmailConfirmation WHERE userId=?")) {
                statement.setLong(1, user.getId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return toEmailConfirmation(statement.getMetaData(), resultSet);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find EmailConfirmation by User.", e);
        }
    }

    @Override
    public EmailConfirmation findBySecret(String secret) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM EmailConfirmation WHERE secret=?")) {
                statement.setString(1, secret);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return toEmailConfirmation(statement.getMetaData(), resultSet);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find EmailConfirmation by secret.", e);
        }
    }

    @Override
    public List<EmailConfirmation> findAll() {
        List<EmailConfirmation> confirmations = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM EmailConfirmation ORDER BY id")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        confirmations.add(toEmailConfirmation(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find all email confirmations.", e);
        }
        return confirmations;
    }

    @Override
    public void save(EmailConfirmation confirmation) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO EmailConfirmation (userId, secret, creationTime) VALUES (?, ?, NOW())",
                    Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, confirmation.getUserId());
                statement.setString(2, confirmation.getSecret());
                if (statement.executeUpdate() == 1) {
                    ResultSet generatedIdResultSet = statement.getGeneratedKeys();
                    if (generatedIdResultSet.next()) {
                        confirmation.setId(generatedIdResultSet.getLong(1));
                        confirmation.setCreationTime(findCreationTime(confirmation.getId()));
                    } else {
                        throw new RepositoryException("Can't find id of saved EmailConfirmation.");
                    }
                } else {
                    throw new RepositoryException("Can't save EmailConfirmation.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save EmailConfirmation.", e);
        }
    }

    @Override
    public void delete(EmailConfirmation confirmation) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM EmailConfirmation WHERE id=?")) {
                statement.setLong(1, confirmation.getId());
                if (statement.executeUpdate() == 1) {
                    //deleted correctly
                } else {
                    throw new RepositoryException("Deleted more than one EmailConfirmation.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't delete EmailConfirmation.", e);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private EmailConfirmation toEmailConfirmation(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        EmailConfirmation confirmation = new EmailConfirmation();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            if ("id".equalsIgnoreCase(columnName)) {
                confirmation.setId(resultSet.getLong(i));
            } else if ("userId".equalsIgnoreCase(columnName)) {
                confirmation.setUserId(resultSet.getLong(i));
            } else if ("secret".equalsIgnoreCase(columnName)) {
                confirmation.setSecret(resultSet.getString(i));
            } else if ("creationTime".equalsIgnoreCase(columnName)) {
                confirmation.setCreationTime(resultSet.getTimestamp(i));
            } else {
                throw new RepositoryException("Unexpected column 'EmailConfirmation." + columnName + "'.");
            }
        }
        return confirmation;
    }

    private Date findCreationTime(long confirmationId) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT creationTime FROM EmailConfirmation WHERE id=?")) {
                statement.setLong(1, confirmationId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getTimestamp(1);
                    }
                }
                throw new RepositoryException("Can't find EmailConfirmation.creationTime by id.");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find EmailConfirmation.creationTime by id.", e);
        }
    }
}
