package com.zozak.blog.service.impl;

import com.zozak.blog.domain.entity.Post;
import com.zozak.blog.domain.entity.Tag;
import com.zozak.blog.repository.TagRepository;
import com.zozak.blog.service.TagService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByNameInIgnoreCase(tagNames);

        Set<String> existingTagNames = existingTags
            .stream()
            .map(Tag::getName)
            .collect(Collectors.toSet());

        List<Tag> newTags = tagNames
            .stream()
            .filter(name -> !existingTagNames.contains(name))
            .map(name ->
                Tag.builder().name(name).posts(new HashSet<Post>()).build()
            )
            .toList();

        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
        }

        savedTags.addAll(existingTags);

        return savedTags;
    }

    @Override
    @Transactional
    public void deleteTag(UUID tagId) {
        tagRepository.findById(tagId).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete tag with posts");
            }
            tagRepository.deleteById(tagId);
        });
    }
}
