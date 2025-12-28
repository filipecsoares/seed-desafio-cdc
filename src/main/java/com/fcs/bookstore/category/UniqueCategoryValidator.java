package com.fcs.bookstore.category;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueCategoryValidator implements ConstraintValidator<UniqueCategory, String> {

    private final CategoryRepository categoryRepository;

    public UniqueCategoryValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // let @NotBlank handle required
        }
        return !categoryRepository.existsByName(value);
    }
}
