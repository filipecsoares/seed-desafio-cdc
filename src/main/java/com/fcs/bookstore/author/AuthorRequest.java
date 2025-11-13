package com.fcs.bookstore.author;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record AuthorRequest(
        @NotBlank(message = "name is required") @Size(min = 2, message = "name must have at least 2 characters") String name,
        @NotBlank(message = "email is required") @Email(message = "email must be a well-formed email address") @Size(max = 200, message = "email must be at most 200 characters") String email,
        @NotBlank(message = "description is required") @Size(max = 400, message = "description must be at most 400 characters") String description
) {
    public Author toModel() {
        return new Author(null, name, email, description, Instant.now());
    }
}
