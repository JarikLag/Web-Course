package ru.itmo.wm4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.wm4.domain.Comment;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM comment WHERE notice_id=?1", nativeQuery = true)
    List<Comment> findByNoticeOrderByCreationTime(long notice_id);
}
