package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.Event;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.repository.EventRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventRepositoryImpl implements EventRepository {
    private static final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public Event find(long eventId) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Event WHERE id=?")) {
                statement.setLong(1, eventId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return toEvent(statement.getMetaData(), resultSet);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Event by id.", e);
        }
    }

    @Override
    public List<Event> findByUser(User user) {
        List<Event> events = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Event WHERE userId=?")) {
                statement.setLong(1, user.getId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        events.add(toEvent(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Event by User.", e);
        }
        return events;
    }

    @Override
    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Event ORDER BY id")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        events.add(toEvent(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find all events.", e);
        }
        return events;
    }

    @Override
    public void save(Event event) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Event (userId, type, creationTime) VALUES (?, ?, NOW())",
                    Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, event.getUserId());
                statement.setString(2, event.getEventType());
                if (statement.executeUpdate() == 1) {
                    ResultSet generatedIdResultSet = statement.getGeneratedKeys();
                    if (generatedIdResultSet.next()) {
                        event.setId(generatedIdResultSet.getLong(1));
                        event.setCreationTime(findCreationTime(event.getId()));
                    } else {
                        throw new RepositoryException("Can't find id of saved Event.");
                    }
                } else {
                    throw new RepositoryException("Can't save Event.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save User.", e);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private Event toEvent(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        Event event = new Event();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            if ("id".equalsIgnoreCase(columnName)) {
                event.setId(resultSet.getLong(i));
            } else if ("userId".equalsIgnoreCase(columnName)) {
                event.setUserId(resultSet.getLong(i));
            } else if ("type".equalsIgnoreCase(columnName)) {
                event.setEventType(resultSet.getString(i));
            } else if ("creationTime".equalsIgnoreCase(columnName)) {
                event.setCreationTime(resultSet.getTimestamp(i));
            } else {
                throw new RepositoryException("Unexpected column 'Event." + columnName + "'.");
            }
        }
        return event;
    }

    private Date findCreationTime(long eventId) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT creationTime FROM Event WHERE id=?")) {
                statement.setLong(1, eventId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getTimestamp(1);
                    }
                }
                throw new RepositoryException("Can't find Event.creationTime by id.");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Event.creationTime by id.", e);
        }
    }
}
