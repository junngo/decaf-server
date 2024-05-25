package com.app.decaf.repository;

import com.app.decaf.model.Category;
import com.app.decaf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserAndDeletedIsFalse(User user);
    Optional<Category> findByIdAndUserAndDeletedIsFalse(Long id, User user);
}
