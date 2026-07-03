package com.zozak.blog.service;

import com.zozak.blog.domain.entity.Tag;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    List<Tag> getAllTags();
    List<Tag> createTags(Set<String> tagNames);
    void deleteTag(UUID tagId);
}
