package com.igar15.training_management.service.impl;

import com.igar15.training_management.AbstractServiceTest;
import com.igar15.training_management.testdata.ExerciseTypeTestData;
import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.entity.enums.Measure;
import com.igar15.training_management.exceptions.ExerciseTypeExistException;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.service.ExerciseTypeService;
import com.igar15.training_management.to.ExerciseTypeTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.igar15.training_management.testdata.ExerciseTypeTestData.*;
import static com.igar15.training_management.testdata.UserTestData.*;
import static org.assertj.core.api.Assertions.*;

class ExerciseTypeServiceImplTest extends AbstractServiceTest {

    @Autowired
    private ExerciseTypeService exerciseTypeService;

    @Test
    void getExercisesTypes() {
        List<ExerciseType> exercisesTypes = exerciseTypeService.getExercisesTypes(USER1_ID);
        assertThat(exercisesTypes).usingRecursiveComparison()
                .ignoringFields("user").isEqualTo(List.of(USER1_EXERCISE_TYPE1, USER1_EXERCISE_TYPE2, USER1_EXERCISE_TYPE3));
    }

    @Test
    void getExerciseTypeById() {
        ExerciseType exerciseType = exerciseTypeService.getExerciseTypeById(USER1_EXERCISE_TYPE1.getId(), USER1_ID);
        assertThat(exerciseType).usingRecursiveComparison()
                .ignoringFields("user").isEqualTo(USER1_EXERCISE_TYPE1);
    }

    @Test
    void getExerciseTypeByIdWhenNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseTypeService.getExerciseTypeById(ExerciseTypeTestData.NOT_FOUND_EXERCISE_TYPE_ID, USER1_ID));
    }

    @Test
    void getExerciseTypeByIdNotOwn() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseTypeService.getExerciseTypeById(USER1_EXERCISE_TYPE1_ID, USER2_ID));
    }

    @Test
    void createExerciseType() {
        ExerciseTypeTo newExerciseTypeTo = getNewExerciseTypeTo();
        ExerciseType newExerciseType = getNewExerciseType();
        ExerciseType createdExerciseType = exerciseTypeService.createExerciseType(newExerciseTypeTo, USER1_ID);
        Long createdId = createdExerciseType.getId();
        newExerciseType.setId(createdId);
        assertThat(createdExerciseType).usingRecursiveComparison()
                .ignoringFields("user").isEqualTo(newExerciseType);
        assertThat(exerciseTypeService.getExerciseTypeById(createdId, USER1_ID)).usingRecursiveComparison()
                .ignoringFields("user").isEqualTo(newExerciseType);
    }

    @Test
    void createExerciseTypeWithExistingName() {
        ExerciseTypeTo newExerciseTypeTo = getNewExerciseTypeTo();
        newExerciseTypeTo.setName(USER1_EXERCISE_TYPE1.getName());
        Assertions.assertThrows(ExerciseTypeExistException.class, () -> exerciseTypeService.createExerciseType(newExerciseTypeTo, USER1_ID));
    }

    @Test
    void createExerciseTypeWithExistingNameForDifferentUsers() {
        ExerciseTypeTo newExerciseTypeTo = getNewExerciseTypeTo();
        newExerciseTypeTo.setName(USER1_EXERCISE_TYPE1.getName());
        ExerciseType exerciseType = exerciseTypeService.createExerciseType(newExerciseTypeTo, ADMIN_ID);
        assertThat(exerciseType).usingRecursiveComparison()
                .ignoringFields("user").isEqualTo(exerciseTypeService.getExerciseTypeById(exerciseType.getId(), ADMIN_ID));
    }

    @Test
    void createExerciseTypeWithNotValidAttributes() {
        validateRootCause(() -> exerciseTypeService.createExerciseType(new ExerciseTypeTo(null, null, Measure.TIMES.toString()), USER1_ID), ConstraintViolationException.class);
        validateRootCause(() -> exerciseTypeService.createExerciseType(new ExerciseTypeTo(null, "", Measure.TIMES.toString()), USER1_ID), ConstraintViolationException.class);
        validateRootCause(() -> exerciseTypeService.createExerciseType(new ExerciseTypeTo(null, "a", Measure.TIMES.toString()), USER1_ID), ConstraintViolationException.class);
        validateRootCause(() -> exerciseTypeService.createExerciseType(new ExerciseTypeTo(null, "name", null), USER1_ID), ConstraintViolationException.class);
        validateRootCause(() -> exerciseTypeService.createExerciseType(new ExerciseTypeTo(null, "name", "NOT_EXISTING"), USER1_ID), IllegalArgumentException.class);
    }


    @Test
    void updateExerciseType() {
        ExerciseTypeTo updatedExerciseTypeTo = getUpdatedExerciseTypeTo();
        ExerciseType updatedExerciseTypeExpected = getUpdatedExerciseType();
        exerciseTypeService.updateExerciseType(updatedExerciseTypeTo, USER1_ID);
        assertThat(exerciseTypeService.getExerciseTypeById(updatedExerciseTypeExpected.getId(), USER1_ID)).usingRecursiveComparison()
                .ignoringFields("user").isEqualTo(updatedExerciseTypeExpected);
    }

    @Test
    void updateExerciseTypeWithExistingName() {
        ExerciseTypeTo updatedExerciseTypeTo = getUpdatedExerciseTypeTo();
        updatedExerciseTypeTo.setName("user1 exercise type 2");
        Assertions.assertThrows(ExerciseTypeExistException.class, () -> exerciseTypeService.updateExerciseType(updatedExerciseTypeTo, USER1_ID));
    }

    @Test
    void updateExerciseTypeWhenNameNotChange() {
        ExerciseTypeTo updatedExerciseTypeTo = getUpdatedExerciseTypeTo();
        ExerciseType updatedExerciseTypeExpected = getUpdatedExerciseType();
        updatedExerciseTypeTo.setName("user1 exercise type 1");
        updatedExerciseTypeExpected.setName("user1 exercise type 1");
        exerciseTypeService.updateExerciseType(updatedExerciseTypeTo, USER1_ID);
        assertThat(exerciseTypeService.getExerciseTypeById(updatedExerciseTypeExpected.getId(), USER1_ID)).usingRecursiveComparison()
                .ignoringFields("user").isEqualTo(updatedExerciseTypeExpected);
    }

    @Test
    void updateExerciseTypeNotOwn() {
        ExerciseTypeTo updatedExerciseTypeTo = getUpdatedExerciseTypeTo();
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseTypeService.updateExerciseType(updatedExerciseTypeTo, USER2_ID));
    }

    @Test
    void deleteExerciseType() {
        exerciseTypeService.deleteExerciseType(USER1_EXERCISE_TYPE1_ID, USER1_ID);
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseTypeService.getExerciseTypeById(USER1_EXERCISE_TYPE1_ID, USER1_ID));
    }

    @Test
    void deleteExerciseTypeWhenNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseTypeService.deleteExerciseType(ExerciseTypeTestData.NOT_FOUND_EXERCISE_TYPE_ID, USER1_ID));
    }

    @Test
    void deleteExerciseTypeNotOwn() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseTypeService.deleteExerciseType(USER1_EXERCISE_TYPE1_ID, USER2_ID));
    }
}