package com.igar15.training_management.service.impl;

import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.repository.ExerciseRepository;
import com.igar15.training_management.service.ExerciseService;
import com.igar15.training_management.service.ExerciseTypeService;
import com.igar15.training_management.service.WorkoutService;
import com.igar15.training_management.to.ExerciseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private ExerciseTypeService exerciseTypeService;

    @Override
    public List<Exercise> getExercisesByWorkoutIdAndUserId(long workoutId, long userId) {
        return exerciseRepository.findAllByWorkout_IdAndWorkout_User_Id(workoutId, userId);
    }

    @Override
    public Exercise getExerciseByIdAndUserId(long id, long userId) {
        return exerciseRepository.findByIdAndWorkout_User_Id(id, userId).orElseThrow(() -> new MyEntityNotFoundException("Not found exercise with id: " + id));
    }

    @Override
    public Exercise createExercise(ExerciseTo exerciseTo, long userId) {
        Assert.notNull(exerciseTo, "Exercise must not be null");
        Workout workout = workoutService.getWorkoutById(exerciseTo.getWorkoutId(), userId);
        ExerciseType exerciseType = exerciseTypeService.getExerciseTypeById(exerciseTo.getExerciseTypeId(), userId);
        Exercise exercise = new Exercise();
        exercise.setWorkout(workout);
        exercise.setExerciseType(exerciseType);
        exercise.setQuantity(exerciseTo.getQuantity());
        exerciseRepository.save(exercise);
        return exercise;
    }

    @Override
    public Exercise updateExercise(ExerciseTo exerciseTo, long userId) {
        Assert.notNull(exerciseTo, "Exercise must not be null");
        long id = exerciseTo.getId();
        Exercise exercise = exerciseRepository.findByIdAndWorkout_User_Id(id, userId).orElseThrow(() -> new MyEntityNotFoundException("Not found exercise with id: " + id));
        exercise.setQuantity(exerciseTo.getQuantity());
        exerciseRepository.save(exercise);
        return exercise;
    }

    @Override
    public void deleteExercise(long id, long userId) {
        Exercise exercise = getExerciseByIdAndUserId(id, userId);
        exerciseRepository.delete(exercise);
    }
}
