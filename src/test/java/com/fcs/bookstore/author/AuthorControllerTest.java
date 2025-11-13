package com.fcs.bookstore.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fcs.bookstore.config.exception.GlobalExceptionHandler;


@WebMvcTest(AuthorController.class)
@Import(GlobalExceptionHandler.class)
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
    void shouldReturnBadRequestWhenInvalidJson() throws Exception {
        // Given
        String invalidJson = "{ \"name\": \"John Doe\", \"email\": }";

        // When & Then
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
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

    // New tests for validation of required fields and invalid email
    @Test
    void shouldReturnBadRequestWhenNameBlank() throws Exception {
        // Given
        AuthorRequest authorRequest = new AuthorRequest(
                "",
                "john.doe@example.com",
                "Award-winning author"
        );

        // When & Then
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("name is required"));
    }

    @Test
    void shouldReturnBadRequestWhenNameMissingInJson() throws Exception {
        // Given: JSON without the `name` field
        String jsonMissingName = "{ \"email\": \"john.doe@example.com\", \"description\": \"Award-winning author\" }";

        // When & Then
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMissingName))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("name is required"));
    }

    @Test
    void shouldReturnBadRequestWhenEmailInvalid() throws Exception {
        // Given
        AuthorRequest authorRequest = new AuthorRequest(
                "John Doe",
                "not-an-email",
                "Award-winning author"
        );

        // When & Then
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("email must be a well-formed email address"));
    }

    @Test
    void shouldReturnBadRequestWhenDescriptionBlank() throws Exception {
        // Given
        AuthorRequest authorRequest = new AuthorRequest(
                "John Doe",
                "john.doe@example.com",
                ""
        );

        // When & Then
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("description is required"));
    }
}