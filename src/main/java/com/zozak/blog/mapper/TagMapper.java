package com.zozak.blog.mapper;

import com.zozak.blog.domain.PostStatus;
import com.zozak.blog.domain.dto.TagResponse;
import com.zozak.blog.domain.entity.Post;
import com.zozak.blog.domain.entity.Tag;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TagMapper {
    @Mapping(
        target = "postCount",
        source = "posts",
        qualifiedByName = "calculatePostCount"
    )
    TagResponse toTagResponse(Tag tag);

    @Named("calculatePostCount")
    default int calculatePostCount(Set<Post> posts) {
        if (posts == null) {
            return 0;
        }
        return (int) posts
            .stream()
            .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
            .count();
    }
}
