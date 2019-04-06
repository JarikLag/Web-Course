package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Talk;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.TalkRepository;
import ru.itmo.webmail.model.repository.impl.TalkRepositoryImpl;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class TalkService {
    private TalkRepository talkRepository = new TalkRepositoryImpl();

    public void addTalk(long sourceId, long targetId, String text) {
        Talk talk = new Talk();
        talk.setSourceUserId(sourceId);
        talk.setTargetUserId(targetId);
        talk.setText(text);
        talkRepository.save(talk);
    }

    public Talk find(long talkId) {
        return talkRepository.find(talkId);
    }

    public List<Talk> findAll() {
        return talkRepository.findAll();
    }

    public List<Talk> findBySourceUser(User user) {
        return talkRepository.findBySourceUser(user);
    }

    public List<Talk> findByTargetUser(User user) {
        return talkRepository.findByTargetUser(user);
    }
}
