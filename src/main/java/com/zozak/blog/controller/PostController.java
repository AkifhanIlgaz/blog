package com.zozak.blog.controller;

import com.zozak.blog.domain.dto.CreatePostRequestDto;
import com.zozak.blog.domain.dto.PostDto;
import com.zozak.blog.domain.dto.UpdatePostRequest;
import com.zozak.blog.domain.dto.UpdatePostRequestDto;
import com.zozak.blog.domain.entity.Post;
import com.zozak.blog.domain.entity.User;
import com.zozak.blog.mapper.PostMapper;
import com.zozak.blog.service.PostService;
import com.zozak.blog.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
        @RequestParam(required = false) UUID categoryId,
        @RequestParam(required = false) UUID tagId
    ) {
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();

        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(
        @RequestAttribute UUID userId
    ) {
        User loggedInUser = userService.getUserById(userId);

        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos = draftPosts
            .stream()
            .map(postMapper::toDto)
            .toList();

        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
        @RequestAttribute UUID userId,
        @RequestBody @Valid CreatePostRequestDto request
    ) {
        var user = userService.getUserById(userId);
        var createPostRequest = postMapper.toCreatePostRequest(request);

        var post = postService.createPost(createPostRequest, user);
        var postDto = postMapper.toDto(post);

        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
        @PathVariable UUID id,
        @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto
    ) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(
            updatePostRequestDto
        );

        Post updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto updatedPostDto = postMapper.toDto(updatedPost);

        return ResponseEntity.ok(updatedPostDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id) {
        Post post = postService.getPost(id);
        PostDto postDto = postMapper.toDto(post);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
