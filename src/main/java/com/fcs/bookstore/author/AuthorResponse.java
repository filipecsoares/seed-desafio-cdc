package com.fcs.bookstore.author;

public record AuthorResponse(Long id, String name, String description) {
    public static AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getDescription()
        );
    }
}
