package com.igar15.training_management.service;

import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.to.WorkoutTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkoutService {

    Page<Workout> getWorkouts(Pageable pageable, long userId);

    Workout getWorkoutById(long id, long userId);

    Workout createWorkout(WorkoutTo workoutTo, long userId);

    Workout updateWorkout(WorkoutTo workoutTo, long userId);

    void deleteWorkout(long id, long userId);
}
