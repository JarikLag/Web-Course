package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.Event;
import ru.itmo.webmail.model.domain.Talk;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.repository.TalkRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TalkRepositoryImpl implements TalkRepository {
    private static final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public Talk find(long talkId) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Talk WHERE id=?")) {
                statement.setLong(1, talkId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return toTalk(statement.getMetaData(), resultSet);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Talk by id.", e);
        }
    }

    @Override
    public List<Talk> findBySourceUser(User user) {
        List<Talk> talks = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Talk WHERE sourceUserId=?")) {
                statement.setLong(1, user.getId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        talks.add(toTalk(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Talk by Source User.", e);
        }
        return talks;
    }

    @Override
    public List<Talk> findByTargetUser(User user) {
        List<Talk> talks = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Talk WHERE targetUserId=?")) {
                statement.setLong(1, user.getId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        talks.add(toTalk(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Talk by Target User.", e);
        }
        return talks;
    }

    @Override
    public List<Talk> findAll() {
        List<Talk> talks = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Talk ORDER BY id")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        talks.add(toTalk(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find all talks.", e);
        }
        return talks;
    }

    @Override
    public void save(Talk talk) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Talk (sourceUserId, targetUserId, text, creationTime) VALUES (?, ?, ?, NOW())",
                    Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, talk.getSourceUserId());
                statement.setLong(2, talk.getTargetUserId());
                statement.setString(3, talk.getText());
                if (statement.executeUpdate() == 1) {
                    ResultSet generatedIdResultSet = statement.getGeneratedKeys();
                    if (generatedIdResultSet.next()) {
                        talk.setId(generatedIdResultSet.getLong(1));
                        talk.setCreationTime(findCreationTime(talk.getId()));
                    } else {
                        throw new RepositoryException("Can't find id of saved Talk.");
                    }
                } else {
                    throw new RepositoryException("Can't save Talk.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save Talk.", e);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private Talk toTalk(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        Talk talk = new Talk();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            if ("id".equalsIgnoreCase(columnName)) {
                talk.setId(resultSet.getLong(i));
            } else if ("sourceUserId".equalsIgnoreCase(columnName)) {
                talk.setSourceUserId(resultSet.getLong(i));
            } else if ("targetUserId".equalsIgnoreCase(columnName)) {
                talk.setTargetUserId(resultSet.getLong(i));
            } else if ("text".equalsIgnoreCase(columnName)) {
                talk.setText(resultSet.getString(i));
            } else if ("creationTime".equalsIgnoreCase(columnName)) {
                talk.setCreationTime(resultSet.getTimestamp(i));
            } else {
                throw new RepositoryException("Unexpected column 'Talk." + columnName + "'.");
            }
        }
        return talk;
    }

    private Date findCreationTime(long talkId) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT creationTime FROM Talk WHERE id=?")) {
                statement.setLong(1, talkId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getTimestamp(1);
                    }
                }
                throw new RepositoryException("Can't find Talk.creationTime by id.");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Talk.creationTime by id.", e);
        }
    }
}
