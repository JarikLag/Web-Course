package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Event;
import ru.itmo.webmail.model.domain.User;

import java.util.List;

public interface EventRepository {
    Event find(long eventId);
    List<Event> findByUser(User user);
    List<Event> findAll();
    void save(Event event);
}