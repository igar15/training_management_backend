package com.igar15.training_management.utils;

import com.igar15.training_management.exceptions.IllegalRequestDataException;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationUtil {

    public static void validateTo(BindingResult bindingResult, String... ignoreFields) {
        if (bindingResult.hasErrors()) {
            List<String> ignoreFieldsList = List.of(ignoreFields);
            String validationErrors = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> !ignoreFieldsList.contains(fieldError.getField()))
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            if (!validationErrors.isEmpty()) {
                throw new IllegalRequestDataException("Please correct these errors: " + validationErrors);
            }
        }
    }
}
