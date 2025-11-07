package com.fcs.bookstore.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnCreatedAuthorWhenValidRequest() throws Exception {
        // Given
        AuthorRequest authorRequest = new AuthorRequest(
                "John Doe",
                "john.doe@example.com",
                "Award-winning author"
        );

        Author savedAuthor = new Author(
                1L,
                "John Doe",
                "john.doe@example.com",
                "Award-winning author",
                Instant.now()
        );

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        // When & Then
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.description").value("Award-winning author"));
    }

    @Test
    void shouldReturnCreatedAuthorWhenMinimalRequest() throws Exception {
        // Given
        AuthorRequest authorRequest = new AuthorRequest(
                "Jane Smith",
                "jane.smith@example.com",
                null
        );

        Author savedAuthor = new Author(
                2L,
                "Jane Smith",
                "jane.smith@example.com",
                null,
                Instant.now()
        );

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        // When & Then
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.description").isEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidJson() throws Exception {
        // Given
        String invalidJson = "{ \"name\": \"John Doe\", \"email\": }";

        // When & Then
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnsupportedMediaTypeWhenWrongContentType() throws Exception {
        // Given
        AuthorRequest authorRequest = new AuthorRequest(
                "John Doe",
                "john.doe@example.com",
                "Award-winning author"
        );

        // When & Then
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isUnsupportedMediaType());
    }
}