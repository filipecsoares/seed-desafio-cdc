package com.fcs.bookstore.category;

import com.fcs.bookstore.shared.UniqueValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "name is required") @Size(max = 300) @UniqueValue(domainClass = Category.class, fieldName = "name") String name
) {
    public Category toModel() {
        return new Category(null, name);
    }
}
