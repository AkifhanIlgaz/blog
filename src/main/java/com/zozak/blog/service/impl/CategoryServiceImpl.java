package com.zozak.blog.service.impl;

import com.zozak.blog.domain.entity.Category;
import com.zozak.blog.repository.CategoryRepository;
import com.zozak.blog.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException(
                "Category already exists with name: " + category.getName()
            );
        }

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = getCategoryById(id);

        if (!category.getPosts().isEmpty()) {
            throw new IllegalStateException(
                "Cannot delete category: " +
                    category.getName() +
                    ". It has associated posts."
            );
        }

        categoryRepository.delete(category);
    }

    private Category getCategoryById(UUID id) {
        return categoryRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException("Category not found with id: " + id)
            );
    }
}
