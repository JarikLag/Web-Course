package ru.itmo.wm4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.wm4.domain.Tag;

public interface TagsRepository extends JpaRepository<Tag, Long> {
    int countByName(String name);

    @Query(value = "SELECT * FROM tag WHERE name=?1", nativeQuery = true)
    Tag findByName(String name);
}
