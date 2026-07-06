package com.zozak.blog.service;

import com.zozak.blog.domain.dto.CreatePostRequest;
import com.zozak.blog.domain.dto.UpdatePostRequest;
import com.zozak.blog.domain.entity.Post;
import com.zozak.blog.domain.entity.User;
import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(CreatePostRequest request, User user);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    Post getPost(UUID id);
    void deletePost(UUID id);
}
