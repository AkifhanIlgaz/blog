package com.zozak.blog.service.impl;

import com.zozak.blog.domain.PostStatus;
import com.zozak.blog.domain.entity.Category;
import com.zozak.blog.domain.entity.Post;
import com.zozak.blog.domain.entity.Tag;
import com.zozak.blog.domain.entity.User;
import com.zozak.blog.repository.PostRepository;
import com.zozak.blog.service.CategoryService;
import com.zozak.blog.service.PostService;
import com.zozak.blog.service.TagService;
import java.util.List;
import java.util.UUID;
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
}
