package ru.itmo.wm4.service;

import org.springframework.stereotype.Service;
import ru.itmo.wm4.domain.Comment;
import ru.itmo.wm4.domain.Notice;
import ru.itmo.wm4.domain.User;
import ru.itmo.wm4.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public List<Comment> findByNoticeOrderByCreationTime(long noticeId) {
        return commentRepository.findByNoticeOrderByCreationTime(noticeId);
    }

    public void save(Comment comment, Notice notice, User user) {
        comment.setNotice(notice);
        comment.setUser(user);
        notice.addComment(comment);
        user.addComment(comment);
        commentRepository.save(comment);
    }
}
