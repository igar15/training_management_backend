package com.igar15.training_management.service.impl;

import com.igar15.training_management.AbstractServiceTest;
import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.entity.User;
import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.service.ExerciseService;
import com.igar15.training_management.to.ExerciseTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.igar15.training_management.ExerciseTestData.*;
import static com.igar15.training_management.ExerciseTypeTestData.*;
import static com.igar15.training_management.UserTestData.*;
import static com.igar15.training_management.WorkoutTestData.*;
import static org.assertj.core.api.Assertions.*;

class ExerciseServiceImplTest extends AbstractServiceTest {

    @Autowired
    private ExerciseService exerciseService;

    @Test
    void getExercisesByWorkoutIdAndUserId() {
        List<Exercise> exercises = exerciseService.getExercisesByWorkoutIdAndUserId(USER1_WORKOUT1_ID, USER1_ID);
        assertThat(exercises).usingElementComparatorIgnoringFields("workout", "user").isEqualTo(List.of(USER1_WORKOUT1_EXERCISE1, USER1_WORKOUT1_EXERCISE2, USER1_WORKOUT1_EXERCISE3));
    }

    @Test
    void getExerciseByIdAndUserId() {
        Exercise exercise = exerciseService.getExerciseByIdAndWorkoutIdAndUserId(USER1_WORKOUT1_EXERCISE1_ID, USER1_WORKOUT1_ID, USER1_ID);
        assertThat(exercise).isEqualTo(USER1_WORKOUT1_EXERCISE1);
    }

    @Test
    void getExerciseByIdAndUserIdWhereNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.getExerciseByIdAndWorkoutIdAndUserId(NOT_FOUND_EXERCISE_ID, USER1_WORKOUT1_ID, USER1_ID));
    }

    @Test
    void getExerciseByIdAndUserIdNotOwn() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.getExerciseByIdAndWorkoutIdAndUserId(USER1_WORKOUT1_EXERCISE1_ID, USER1_WORKOUT1_ID, USER2_ID));
    }

    @Test
    void createExercise() {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        Exercise newExercise = getNewExercise();
        Exercise createdExercise = exerciseService.createExercise(newExerciseTo, USER1_ID);
        Long createdId = createdExercise.getId();
        newExercise.setId(createdId);
        assertThat(createdExercise).isEqualTo(newExercise);
        Exercise exercise = exerciseService.getExerciseByIdAndWorkoutIdAndUserId(createdId, USER1_WORKOUT1_ID, USER1_ID);
        assertThat(exercise).isEqualTo(newExercise);
    }

    @Test
    void createExerciseWithNotOwnWorkout() {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        newExerciseTo.setWorkoutId(ADMIN_WORKOUT1_ID);
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.createExercise(newExerciseTo, USER1_ID));
    }

    @Test
    void createExerciseWithNotOwnExerciseType() {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        newExerciseTo.setExerciseTypeId(ADMIN_EXERCISE_TYPE1_ID);
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.createExercise(newExerciseTo, USER1_ID));
    }

    @Test
    void createExerciseWithNotValidAttributes() {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        newExerciseTo.setQuantity(0);
        validateRootCause(() -> exerciseService.createExercise(newExerciseTo, USER1_ID), ConstraintViolationException.class);
    }

    @Test
    void updateExercise() {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        Exercise updatedExerciseExpected = getUpdatedExercise();
        exerciseService.updateExercise(updatedExerciseTo, USER1_WORKOUT1_ID, USER1_ID);
        assertThat(exerciseService.getExerciseByIdAndWorkoutIdAndUserId(updatedExerciseExpected.getId(), USER1_WORKOUT1_ID, USER1_ID)).isEqualTo(updatedExerciseExpected);
    }

    @Test
    void updateExerciseNotOwn() {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.updateExercise(updatedExerciseTo, USER1_WORKOUT1_ID, USER2_ID));
    }

    @Test
    void deleteExercise() {
        exerciseService.deleteExercise(USER1_WORKOUT1_EXERCISE1_ID, USER1_WORKOUT1_ID, USER1_ID);
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.getExerciseByIdAndWorkoutIdAndUserId(USER1_WORKOUT1_EXERCISE1_ID, USER1_WORKOUT1_ID, USER1_ID));
    }

    @Test
    void deleteExerciseWhereNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.deleteExercise(NOT_FOUND_EXERCISE_ID, USER1_WORKOUT1_ID, USER1_ID));
    }

    @Test
    void deleteExerciseNotOwn() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.deleteExercise(USER1_WORKOUT1_EXERCISE1_ID, USER1_WORKOUT1_ID, USER2_ID));
    }
}