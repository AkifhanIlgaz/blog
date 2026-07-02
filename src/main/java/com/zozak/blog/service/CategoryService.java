package com.zozak.blog.service;

import com.zozak.blog.domain.entity.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategories();
    Category createCategory(Category category);
    void deleteCategory(UUID id);
}
