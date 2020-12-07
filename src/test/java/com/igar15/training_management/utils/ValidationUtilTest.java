package com.igar15.training_management.utils;

import com.igar15.training_management.UserTestData;
import com.igar15.training_management.exceptions.IllegalRequestDataException;
import com.igar15.training_management.to.UserTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void validateToWithNotValidFields() {
        UserTo newUserTo = UserTestData.getNewUserTo();
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserTo, "newUserTo");
        FieldError nameFieldError = new FieldError("newUserTo", "name", "Name must not be blank");
        bindingResult.addError(nameFieldError);
        Assertions.assertThrows(IllegalRequestDataException.class, () -> ValidationUtil.validateTo(bindingResult));
    }

    @Test
    void validateToWithAllValidFields() {
        UserTo newUserTo = UserTestData.getNewUserTo();
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserTo, "newUserTo");
        Assertions.assertDoesNotThrow(() -> ValidationUtil.validateTo(bindingResult));
    }

    @Test
    void validateToWithNotValidIgnoredFields() {
        UserTo newUserTo = UserTestData.getNewUserTo();
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserTo, "newUserTo");
        FieldError nameFieldError = new FieldError("newUserTo", "name", "Name must not be blank");
        FieldError emailFieldError = new FieldError("newUserTo", "email", "Email must not be blank");
        bindingResult.addError(nameFieldError);
        bindingResult.addError(emailFieldError);
        Assertions.assertDoesNotThrow(() -> ValidationUtil.validateTo(bindingResult, "name", "email"));
    }

    @Test
    void validateToWithNotValidFieldsWhereNotAllIgnored() {
        UserTo newUserTo = UserTestData.getNewUserTo();
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserTo, "newUserTo");
        FieldError nameFieldError = new FieldError("newUserTo", "name", "Name must not be blank");
        FieldError emailFieldError = new FieldError("newUserTo", "email", "Email must not be blank");
        bindingResult.addError(nameFieldError);
        bindingResult.addError(emailFieldError);
        Assertions.assertThrows(IllegalRequestDataException.class, () -> ValidationUtil.validateTo(bindingResult));
    }



}