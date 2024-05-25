package com.app.decaf.service;

import com.app.decaf.config.TestSecurityConfig;
import com.app.decaf.model.Category;
import com.app.decaf.model.User;
import com.app.decaf.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private User user;
    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        category1 = new Category();
        category1.setId(1L);
        category1.setName("Groceries");
        category1.setUser(user);
        category1.setDeleted(false);

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Utilities");
        category2.setUser(user);
        category2.setDeleted(false);
    }

    @Test
    void whenGetAllCategories_thenReturnNonEmptyList() {
        // Given
        List<Category> storedCategories = Arrays.asList(category1, category2);
        given(categoryRepository.findAllByUserAndDeletedIsFalse(user)).willReturn(storedCategories);

        // When
        List<Category> categories = categoryService.getAllCategories(user);

        // Then
        assertThat(categories).isNotEmpty();
        assertThat(categories.size()).isEqualTo(2);
        verify(categoryRepository).findAllByUserAndDeletedIsFalse(user);
    }

    @Test
    void whenCreateCategory_thenReturnSavedCategory() {
        // Given
        given(categoryRepository.save(any(Category.class))).willReturn(category1);

        // When
        Category savedCategory = categoryService.createCategory(category1, user);

        // Then
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Groceries");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void whenUpdateCategory_thenSuccess() {
        // Given
        given(categoryRepository.findByIdAndUserAndDeletedIsFalse(category1.getId(), user)).willReturn(Optional.of(category1));
        given(categoryRepository.save(category1)).willReturn(category1);

        // When
        Category updatedCategory = categoryService.updateCategory(category1.getId(), category1, user);

        // Then
        assertThat(updatedCategory.getName()).isEqualTo("Groceries");
        verify(categoryRepository).save(category1);
    }

    @Test
    void whenDeleteCategory_thenSuccess() {
        // Given
        given(categoryRepository.findByIdAndUserAndDeletedIsFalse(category1.getId(), user)).willReturn(Optional.of(category1));

        // When
        categoryService.deleteCategory(category1.getId(), user);

        // Then
        verify(categoryRepository).save(category1);
        assertThat(category1.isDeleted()).isTrue();
    }
}