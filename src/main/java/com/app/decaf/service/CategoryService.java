package com.app.decaf.service;

import com.app.decaf.model.Category;
import com.app.decaf.model.User;
import com.app.decaf.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories(User user) {
        return categoryRepository.findAllByUserAndDeletedIsFalse(user);
    }

    @Transactional
    public Category createCategory(Category category, User user) {
        category.setUser(user);
        category.setDeleted(false); // Ensure new categories are active by default
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, Category categoryData, User user) {
        Optional<Category> category = categoryRepository.findByIdAndUserAndDeletedIsFalse(id, user);
        if (category.isPresent()) {
            Category existingCategory = category.get();
            existingCategory.setName(categoryData.getName());
            existingCategory.setType(categoryData.getType());
            return categoryRepository.save(existingCategory);
        }
        throw new RuntimeException("Category not found or not authorized");
    }

    @Transactional
    public void deleteCategory(Long id, User user) {
        Optional<Category> category = categoryRepository.findByIdAndUserAndDeletedIsFalse(id, user);
        category.ifPresent(cat -> {
            cat.setDeleted(true);
            categoryRepository.save(cat);
        });
    }
}
