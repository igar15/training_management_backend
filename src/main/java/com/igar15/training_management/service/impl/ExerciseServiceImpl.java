package com.igar15.training_management.service.impl;

import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.repository.ExerciseRepository;
import com.igar15.training_management.repository.UserRepository;
import com.igar15.training_management.service.ExerciseService;
import com.igar15.training_management.service.ExerciseTypeService;
import com.igar15.training_management.service.WorkoutService;
import com.igar15.training_management.to.ExerciseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Exercise> getExercisesByWorkoutIdAndUserId(long workoutId, long userId) {
        workoutService.getWorkoutById(workoutId, userId); // check for own workout
        return exerciseRepository.findAllByWorkout_IdAndUser_Id(workoutId, userId);
    }

    @Override
    public Exercise getExerciseByIdAndWorkoutIdAndUserId(long id, long workoutId, long userId) {
        return exerciseRepository.findByIdAndWorkout_IdAndUser_Id(id, workoutId, userId).orElseThrow(() -> new MyEntityNotFoundException("Not found exercise with id: " + id));
    }

    @Override
    @Transactional
    public Exercise createExercise(ExerciseTo exerciseTo, long userId) {
        Assert.notNull(exerciseTo, "Exercise must not be null");
        Workout workout = workoutService.getWorkoutById(exerciseTo.getWorkoutId(), userId);
        ExerciseType exerciseType = exerciseTypeService.getExerciseTypeById(exerciseTo.getExerciseTypeId(), userId);
        Exercise exercise = new Exercise();
        exercise.setUser(userRepository.getOne(userId));
        exercise.setWorkout(workout);
        exercise.setExerciseType(exerciseType);
        exercise.setQuantity(exerciseTo.getQuantity());
        exerciseRepository.save(exercise);
        return exercise;
    }

    @Override
    @Transactional
    public Exercise updateExercise(ExerciseTo exerciseTo, long workoutId, long userId) {
        Assert.notNull(exerciseTo, "Exercise must not be null");
        workoutService.getWorkoutById(workoutId, userId); // check for own workout
        long id = exerciseTo.getId();
        Exercise exercise = exerciseRepository.findByIdAndWorkout_IdAndUser_Id(id, workoutId, userId).orElseThrow(() -> new MyEntityNotFoundException("Not found exercise with id: " + id));
        exercise.setQuantity(exerciseTo.getQuantity());
        exerciseRepository.save(exercise);
        return exercise;
    }

    @Override
    public void deleteExercise(long id, long workoutId, long userId) {
        Exercise exercise = getExerciseByIdAndWorkoutIdAndUserId(id, workoutId, userId);
        exerciseRepository.delete(exercise);
    }
}
