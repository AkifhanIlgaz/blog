package com.zozak.blog.service.impl;

import com.zozak.blog.domain.PostStatus;
import com.zozak.blog.domain.dto.CreatePostRequest;
import com.zozak.blog.domain.dto.UpdatePostRequest;
import com.zozak.blog.domain.entity.Category;
import com.zozak.blog.domain.entity.Post;
import com.zozak.blog.domain.entity.Tag;
import com.zozak.blog.domain.entity.User;
import com.zozak.blog.repository.PostRepository;
import com.zozak.blog.service.CategoryService;
import com.zozak.blog.service.PostService;
import com.zozak.blog.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                PostStatus.PUBLISHED,
                category,
                tag
            );
        }

        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                PostStatus.PUBLISHED,
                category
            );
        }

        if (tagId != null) {
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                PostStatus.PUBLISHED,
                tag
            );
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(CreatePostRequest request, User user) {
        var postBuilder = Post.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .status(request.getStatus())
            .author(user)
            .readingTime(calculateReadingTime(request.getContent()));

        Category category = categoryService.getCategoryById(
            request.getCategoryId()
        );
        postBuilder.category(category);

        List<Tag> tags = tagService.getTagByIds(request.getTagIds());

        postBuilder.tags(new HashSet<>(tags));

        return postRepository.save(postBuilder.build());
    }

    private int calculateReadingTime(String content) {
        int wordCount = content.split("\\s+").length;
        return (int) Math.ceil(wordCount / 200.0);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException("Post does not exist with id " + id)
            );

        existingPost.setTitle(updatePostRequest.getTitle());
        String postContent = updatePostRequest.getContent();
        existingPost.setContent(postContent);
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(postContent));

        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if (
            !existingPost
                .getCategory()
                .getId()
                .equals(updatePostRequestCategoryId)
        ) {
            Category newCategory = categoryService.getCategoryById(
                updatePostRequestCategoryId
            );
            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existingPost
            .getTags()
            .stream()
            .map(Tag::getId)
            .collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
        if (!existingTagIds.equals(updatePostRequestTagIds)) {
            List<Tag> newTags = tagService.getTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public Post getPost(UUID id) {
        return postRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException("Post does not exist with ID " + id)
            );
    }

    @Transactional
    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }
}
