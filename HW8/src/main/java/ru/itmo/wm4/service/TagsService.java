package ru.itmo.wm4.service;

import org.springframework.stereotype.Service;
import ru.itmo.wm4.domain.Tag;
import ru.itmo.wm4.repository.TagsRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class TagsService {
    private final TagsRepository tagsRepository;

    public TagsService(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    public Set<Tag> save(String[] tagNames) {
        Set<String> stringSet = new HashSet<String>(Arrays.asList(tagNames));
        Set<Tag> tagSet = new HashSet<>();
        for (String now : stringSet) {
            Tag tag;
            if (tagsRepository.countByName(now) == 0) {
                tag = new Tag();
                tag.setName(now);
                tagsRepository.save(tag);
            }
            tag = tagsRepository.findByName(now);
            tagSet.add(tag);
        }
        return tagSet;
    }
}
