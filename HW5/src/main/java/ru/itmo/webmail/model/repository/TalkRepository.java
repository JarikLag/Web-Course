package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Talk;
import ru.itmo.webmail.model.domain.User;

import java.util.List;

public interface TalkRepository {
    Talk find(long talkId);
    List<Talk> findBySourceUser(User user);
    List<Talk> findByTargetUser(User user);
    List<Talk> findAll();
    void save(Talk talk);
}
