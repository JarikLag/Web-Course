package ru.itmo.wm4.service;

import org.springframework.stereotype.Service;
import ru.itmo.wm4.domain.Notice;
import ru.itmo.wm4.domain.Tag;
import ru.itmo.wm4.domain.User;
import ru.itmo.wm4.repository.NoticeRepository;

import java.util.List;
import java.util.Set;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<Notice> findByOrderByCreationTimeDesc() {
        return noticeRepository.findByOrderByCreationTimeDesc();
    }

    public Notice findById(long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    public void save(Notice notice, User user, Set<Tag> tagSet) {
        notice.setTags(tagSet);
        user.addNotice(notice);
        noticeRepository.save(notice);
    }
}
