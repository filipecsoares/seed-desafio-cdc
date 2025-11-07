package com.fcs.bookstore.author;

import java.time.Instant;

public record AuthorRequest(String name, String email, String description) {
    public Author toModel() {
        return new Author(null, name, email, description, Instant.now());
    }
}
