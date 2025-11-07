package com.fcs.bookstore.author;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record AuthorRequest(
        @NotBlank @Size(min = 2) String name,
        @NotBlank @Email @Size(max = 200) String email,
        @NotBlank @Size(max = 400) String description
) {
    public Author toModel() {
        return new Author(null, name, email, description, Instant.now());
    }
}
