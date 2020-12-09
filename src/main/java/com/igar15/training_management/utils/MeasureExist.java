package com.igar15.training_management.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MeasureExistConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MeasureExist {

    String message() default "Measure does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
