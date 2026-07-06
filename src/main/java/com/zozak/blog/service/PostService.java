package com.zozak.blog.service;

import com.zozak.blog.domain.entity.Post;
import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
}
