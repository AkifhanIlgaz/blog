package com.zozak.blog.mapper;

import com.zozak.blog.domain.dto.CreatePostRequest;
import com.zozak.blog.domain.dto.CreatePostRequestDto;
import com.zozak.blog.domain.dto.PostDto;
import com.zozak.blog.domain.dto.UpdatePostRequest;
import com.zozak.blog.domain.dto.UpdatePostRequestDto;
import com.zozak.blog.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PostMapper {
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);
}
