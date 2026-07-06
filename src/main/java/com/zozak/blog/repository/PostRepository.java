package com.zozak.blog.repository;

import com.zozak.blog.domain.PostStatus;
import com.zozak.blog.domain.entity.Category;
import com.zozak.blog.domain.entity.Post;
import com.zozak.blog.domain.entity.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(
        PostStatus status,
        Category category,
        Tag tag
    );
    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);
    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);
    List<Post> findAllByStatus(PostStatus status);
}
