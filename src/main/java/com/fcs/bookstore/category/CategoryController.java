package com.fcs.bookstore.category;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CategoryRequest request) {
        final var category = request.toModel();
        categoryRepository.save(category);
        return ResponseEntity.status(201).build();
    }
}
