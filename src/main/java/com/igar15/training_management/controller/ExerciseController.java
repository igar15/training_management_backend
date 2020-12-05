package com.igar15.training_management.controller;

import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.exceptions.IllegalRequestDataException;
import com.igar15.training_management.service.ExerciseService;
import com.igar15.training_management.to.ExerciseTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class ExerciseController {

    private final Logger log = LoggerFactory.getLogger(ExerciseController.class);

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/{userId}/workouts/{workoutId}/exercises/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable("userId") long userId, @PathVariable("workoutId") long workoutId, @PathVariable("id") long id) {
        log.info("get exercise id={} for workout id={} for user id={}", id, workoutId, userId);
        Exercise exercise = exerciseService.getExerciseByIdAndUserId(id, userId);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }

    @GetMapping("/{userId}/workouts/{workoutId}/exercises")
    public ResponseEntity<List<Exercise>> getExercises(@PathVariable("userId") long userId, @PathVariable("workoutId") long workoutId) {
        log.info("get all exercises for workout id={} for user id={}", workoutId, userId);
        List<Exercise> exercises = exerciseService.getExercisesByWorkoutIdAndUserId(workoutId, userId);
        return new ResponseEntity<>(exercises, HttpStatus.OK);
    }

    @PostMapping("/{userId}/workouts/{workoutId}/exercises")
    public ResponseEntity<Exercise> createExercise(@PathVariable("userId") long userId, @PathVariable("workoutId") Long workoutId, @RequestBody ExerciseTo exerciseTo) {
        if (exerciseTo.getId() != null) {
            throw new IllegalRequestDataException(exerciseTo + " must be new (id=null)");
        }
        if (!workoutId.equals(exerciseTo.getWorkoutId())) {
            throw new IllegalRequestDataException(exerciseTo + " must be with workout id=" + workoutId);
        }
        log.info("create {} for workout id={} for user id={}", exerciseTo, workoutId, userId);
        Exercise exercise = exerciseService.createExercise(exerciseTo, userId);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }

    @PutMapping("/{userId}/workouts/{workoutId}/exercises/{id}")
    public ResponseEntity<Exercise> updateExercise(@PathVariable("userId") long userId, @PathVariable("workoutId") Long workoutId, @PathVariable("id") Long id, @RequestBody ExerciseTo exerciseTo) {
        if (!id.equals(exerciseTo.getId())) {
            throw new IllegalRequestDataException(exerciseTo + " must be with id=" + id);
        }
        if (!workoutId.equals(exerciseTo.getWorkoutId())) {
            throw new IllegalRequestDataException(exerciseTo + " must be with workout id=" + workoutId);
        }
        log.info("update {} for workout id={} for user id={}", exerciseTo, workoutId, userId);
        Exercise exercise = exerciseService.updateExercise(exerciseTo, userId);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/workouts/{workoutId}/exercises/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExercise(@PathVariable("userId") long userId, @PathVariable("workoutId") Long workoutId, @PathVariable("id") Long id) {
        log.info("delete exercise id={} for workout id={} for user id={}", id, workoutId, userId);
        exerciseService.deleteExercise(id, userId);
    }
}