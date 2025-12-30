package com.fcs.bookstore.author;

public record AuthorResponse(Long id, String name, String description) {
    public static AuthorResponse toResponse(Author author) {
        if (author == null) {
            return null;
        }
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getDescription()
        );
    }
}
