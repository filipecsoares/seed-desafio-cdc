package com.fcs.bookstore.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "name is required") @Size(max = 300) @UniqueCategory String name
) {
    public Category toModel() {
        return new Category(null, name);
    }
}
