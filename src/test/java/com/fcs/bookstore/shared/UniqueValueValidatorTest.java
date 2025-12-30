package com.fcs.bookstore.shared;

import com.fcs.bookstore.author.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniqueValueValidatorTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> typedQuery;

    private UniqueValueValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueValueValidator();
        ReflectionTestUtils.setField(validator, "entityManager", entityManager);
        ReflectionTestUtils.setField(validator, "domainAttribute", "name");
        ReflectionTestUtils.setField(validator, "entityClass", Author.class);
    }

    @Test
    void shouldReturnTrueWhenValueIsUnique() {
        // Arrange
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("value", "test")).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(0L);

        // Act
        boolean result = validator.isValid("test", null);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenValueIsNotUnique() {
        // Arrange
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("value", "test")).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(1L);

        // Act
        boolean result = validator.isValid("test", null);

        // Assert
        assertFalse(result);
    }
}