package com.fcs.bookstore.category;

import jakarta.validation.Payload;

public @interface UniqueCategory {
    String message() default "category already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
