package com.app.decaf.cotroller;

import com.app.decaf.model.Category;
import com.app.decaf.model.User;
import com.app.decaf.repository.UserRepository;
import com.app.decaf.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        }
        throw new IllegalStateException("Authentication principal is not of expected type UserDetails");
    }

    // Get all categories for the logged-in user
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        User user = getCurrentUser();
        List<Category> categories = categoryService.getAllCategories(user);
        return ResponseEntity.ok(categories);
    }

    // Create a new category for the logged-in user
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        User user = getCurrentUser();
        Category newCategory = categoryService.createCategory(category, user);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // Update an existing category for the logged-in user
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        User user = getCurrentUser();
        Category updatedCategory = categoryService.updateCategory(id, category, user);
        return ResponseEntity.ok(updatedCategory);
    }

    // Soft delete a category for the logged-in user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        User user = getCurrentUser();
        categoryService.deleteCategory(id, user);
        return ResponseEntity.noContent().build();
    }
}
