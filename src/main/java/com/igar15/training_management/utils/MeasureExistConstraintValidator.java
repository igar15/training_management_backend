package com.igar15.training_management.utils;

import com.igar15.training_management.entity.enums.Measure;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class MeasureExistConstraintValidator implements ConstraintValidator<MeasureExist, String> {

    @Override
    public void initialize(MeasureExist constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(String measure, ConstraintValidatorContext constraintValidatorContext) {
        if (measure == null) {
            return true;
        }
        boolean isMeasureNameValid =  Arrays.stream(Measure.values())
                .anyMatch(enumMeasure -> enumMeasure.toString().equals(measure.toUpperCase()));
        if (!isMeasureNameValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Measure " + measure + " does not exist").addConstraintViolation();
        }
        return isMeasureNameValid;
    }
}
