package com.igar15.training_management.utils;

import com.igar15.training_management.exceptions.IllegalRequestDataException;
import com.igar15.training_management.to.UserTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static com.igar15.training_management.testdata.UserTestData.*;

class ValidationUtilTest {

    @Test
    void validateToWithNotValidFields() {
        UserTo newUserTo = getNewUserTo();
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserTo, "newUserTo");
        FieldError nameFieldError = new FieldError("newUserTo", "name", "Name must not be blank");
        bindingResult.addError(nameFieldError);
        Assertions.assertThrows(IllegalRequestDataException.class, () -> ValidationUtil.validateTo(bindingResult));
    }

    @Test
    void validateToWithAllValidFields() {
        UserTo newUserTo = getNewUserTo();
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserTo, "newUserTo");
        Assertions.assertDoesNotThrow(() -> ValidationUtil.validateTo(bindingResult));
    }

    @Test
    void validateToWithNotValidIgnoredFields() {
        UserTo newUserTo = getNewUserTo();
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserTo, "newUserTo");
        FieldError nameFieldError = new FieldError("newUserTo", "name", "Name must not be blank");
        FieldError emailFieldError = new FieldError("newUserTo", "email", "Email must not be blank");
        bindingResult.addError(nameFieldError);
        bindingResult.addError(emailFieldError);
        Assertions.assertDoesNotThrow(() -> ValidationUtil.validateTo(bindingResult, "name", "email"));
    }

    @Test
    void validateToWithNotValidFieldsWhereNotAllIgnored() {
        UserTo newUserTo = getNewUserTo();
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserTo, "newUserTo");
        FieldError nameFieldError = new FieldError("newUserTo", "name", "Name must not be blank");
        FieldError emailFieldError = new FieldError("newUserTo", "email", "Email must not be blank");
        bindingResult.addError(nameFieldError);
        bindingResult.addError(emailFieldError);
        Assertions.assertThrows(IllegalRequestDataException.class, () -> ValidationUtil.validateTo(bindingResult));
    }

    @Test
    void checkOnNewWhereNew() {
        UserTo newUserTo = getNewUserTo();
        Assertions.assertDoesNotThrow(() -> ValidationUtil.checkOnNew(newUserTo));
    }

    @Test
    void checkOnNewWhereNotNew() {
        UserTo updatedUserTo = getUpdatedUserTo();
        Assertions.assertThrows(IllegalRequestDataException.class, () -> ValidationUtil.checkOnNew(updatedUserTo));
    }

    @Test
    void checkIdTheSameWhereTheSame() {
        UserTo updatedUserTo = getUpdatedUserTo();
        Assertions.assertDoesNotThrow(() -> ValidationUtil.checkIdTheSame(updatedUserTo, USER1_ID));
    }

    @Test
    void checkIdTheSameWhereNotTheSame() {
        UserTo updatedUserTo = getUpdatedUserTo();
        Assertions.assertThrows(IllegalRequestDataException.class, () -> ValidationUtil.checkIdTheSame(updatedUserTo, USER2_ID));
    }



}