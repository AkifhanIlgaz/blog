package com.zozak.blog.controller;

import com.zozak.blog.domain.dto.CreateTagsRequest;
import com.zozak.blog.domain.dto.TagResponse;
import com.zozak.blog.mapper.TagMapper;
import com.zozak.blog.service.TagService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        var tags = tagService
            .getAllTags()
            .stream()
            .map(tagMapper::toTagResponse)
            .toList();
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(
        @RequestBody CreateTagsRequest createTagsRequest
    ) {
        var savedTags = tagService.createTags(createTagsRequest.getNames());
        var tagResponses = savedTags
            .stream()
            .map(tagMapper::toTagResponse)
            .toList();

        return new ResponseEntity<>(tagResponses, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
