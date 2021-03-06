package com.igar15.training_management.service;

import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.to.ExerciseTo;

import java.util.List;

public interface ExerciseService {

    List<Exercise> getExercisesByWorkoutIdAndUserId(long workoutId, long userId);

    Exercise getExerciseByIdAndWorkoutIdAndUserId(long id, long workoutId, long userId);

    Exercise createExercise(ExerciseTo exerciseTo, long userId);

    Exercise updateExercise(ExerciseTo exerciseTo, long workoutId, long userId);

    void deleteExercise(long id, long workoutId, long userId);
}
