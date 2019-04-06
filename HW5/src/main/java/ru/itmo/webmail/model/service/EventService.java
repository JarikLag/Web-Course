package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Event;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.EventRepository;
import ru.itmo.webmail.model.repository.impl.EventRepositoryImpl;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class EventService {
    private EventRepository eventRepository = new EventRepositoryImpl();

    public void addEvent(String eventType, long userId) {
        Event event = new Event();
        event.setUserId(userId);
        event.setEventType(eventType);
        eventRepository.save(event);
    }

    public Event find(long eventId) {
        return eventRepository.find(eventId);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public List<Event> findByUser(User user) {
        return eventRepository.findByUser(user);
    }
}
