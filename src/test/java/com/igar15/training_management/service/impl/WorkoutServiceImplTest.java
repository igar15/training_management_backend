package com.igar15.training_management.service.impl;

import com.igar15.training_management.AbstractServiceTest;
import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.exceptions.WorkoutExistException;
import com.igar15.training_management.service.WorkoutService;
import com.igar15.training_management.to.WorkoutTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.ConstraintViolationException;

import static com.igar15.training_management.testdata.UserTestData.*;
import static com.igar15.training_management.testdata.WorkoutTestData.*;
import static org.assertj.core.api.Assertions.assertThat;

class WorkoutServiceImplTest extends AbstractServiceTest {

    @Autowired
    private WorkoutService workoutService;

    @Test
    void getWorkouts() {
        Page<Workout> workouts = workoutService.getWorkouts(PAGEABLE, USER1_ID);
        assertThat(workouts).isEqualTo(PAGE);
    }

    @Test
    void getWorkoutById() {
        Workout workout = workoutService.getWorkoutById(USER1_WORKOUT1_ID, USER1_ID);
        assertThat(workout).isEqualTo(USER1_WORKOUT1);
    }

    @Test
    void getWorkoutByIdWhereNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> workoutService.getWorkoutById(NOT_FOUND_WORKOUT_ID, USER1_ID));
    }

    @Test
    void getWorkoutByIdNotOwn() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> workoutService.getWorkoutById(USER1_WORKOUT1_ID, USER2_ID));
    }

    @Test
    void createWorkout() {
        WorkoutTo newWorkoutTo = getNewWorkoutTo();
        Workout newWorkout = getNewWorkout();
        Workout createdWorkout = workoutService.createWorkout(newWorkoutTo, USER1_ID);
        Long createdId = createdWorkout.getId();
        newWorkout.setId(createdId);
        assertThat(createdWorkout).isEqualTo(newWorkout);
        assertThat(workoutService.getWorkoutById(createdId, USER1_ID)).isEqualTo(newWorkout);
    }

    @Test
    void createWorkoutWithExistingDateTime() {
        WorkoutTo newWorkoutTo = getNewWorkoutTo();
        newWorkoutTo.setDateTime(USER1_WORKOUT1.getDateTime());
        Assertions.assertThrows(WorkoutExistException.class, () -> workoutService.createWorkout(newWorkoutTo, USER1_ID));
    }

    @Test
    void createWorkoutWithExistingDateTimeForDifferentUsers() {
        WorkoutTo newWorkoutTo = getNewWorkoutTo();
        newWorkoutTo.setDateTime(USER1_WORKOUT1.getDateTime());
        Workout workout = workoutService.createWorkout(newWorkoutTo, ADMIN_ID);
        assertThat(workout).isEqualTo(workoutService.getWorkoutById(workout.getId(), ADMIN_ID));
    }

    @Test
    void createWorkoutWithNotValidAttributes() {
        validateRootCause(() -> workoutService.createWorkout(new WorkoutTo(null, null), USER1_ID), ConstraintViolationException.class);
    }

    @Test
    void updateWorkout() {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        Workout updatedWorkoutExpected = getUpdatedWorkout();
        workoutService.updateWorkout(updatedWorkoutTo, USER1_ID);
        assertThat(workoutService.getWorkoutById(updatedWorkoutExpected.getId(), USER1_ID)).isEqualTo(updatedWorkoutExpected);
    }

    @Test
    void updateWorkoutWithExistingDateTime() {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        updatedWorkoutTo.setDateTime(USER1_WORKOUT2.getDateTime());
        Assertions.assertThrows(WorkoutExistException.class, () -> workoutService.updateWorkout(updatedWorkoutTo, USER1_ID));
    }

    @Test
    void updateWorkoutWhereDateTimeNotChange() {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        Workout updatedWorkoutExpected = getUpdatedWorkout();
        updatedWorkoutTo.setDateTime(USER1_WORKOUT1.getDateTime());
        updatedWorkoutExpected.setDateTime(USER1_WORKOUT1.getDateTime());
        workoutService.updateWorkout(updatedWorkoutTo, USER1_ID);
        assertThat(workoutService.getWorkoutById(updatedWorkoutExpected.getId(), USER1_ID)).isEqualTo(updatedWorkoutExpected);
    }

    @Test
    void updateWorkoutNotOwn() {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> workoutService.updateWorkout(updatedWorkoutTo, USER2_ID));
    }

    @Test
    void deleteWorkout() {
        workoutService.deleteWorkout(USER1_WORKOUT1_ID, USER1_ID);
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> workoutService.getWorkoutById(USER1_WORKOUT1_ID, USER1_ID));
    }

    @Test
    void deleteWorkoutWhereNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> workoutService.deleteWorkout(NOT_FOUND_WORKOUT_ID, USER1_ID));
    }

    @Test
    void deleteWorkoutNotOwn() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> workoutService.deleteWorkout(USER1_WORKOUT1_ID, USER2_ID));
    }
}