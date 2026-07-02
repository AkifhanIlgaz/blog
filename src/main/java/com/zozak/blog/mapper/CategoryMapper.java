package com.zozak.blog.mapper;

import com.zozak.blog.domain.PostStatus;
import com.zozak.blog.domain.dto.CategoryDto;
import com.zozak.blog.domain.dto.CreateCategoryRequest;
import com.zozak.blog.domain.entity.Category;
import com.zozak.blog.domain.entity.Post;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {
    @Mapping(
        target = "postCount",
        source = "posts",
        qualifiedByName = "calculatePostCount"
    )
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (posts == null) {
            return 0;
        }
        return posts
            .stream()
            .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
            .count();
    }
}
