package com.igar15.training_management.controller;

import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.exceptions.IllegalRequestDataException;
import com.igar15.training_management.service.WorkoutService;
import com.igar15.training_management.to.WorkoutTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class WorkoutController {

    private final Logger log = LoggerFactory.getLogger(WorkoutController.class);

    @Autowired
    private WorkoutService workoutService;

    @GetMapping("/{userId}/workouts/{id}")
    public ResponseEntity<Workout> getWorkout(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        log.info("get workout id={} for user id={}", id, userId);
        Workout workout = workoutService.getWorkoutById(id, userId);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @GetMapping("/{userId}/workouts")
    public ResponseEntity<Page<Workout>> getWorkouts(@PathVariable("userId") long userId, Pageable pageable) {
        log.info("get all workouts with pagination=( {} ) for user id={}", pageable, userId);
        Page<Workout> workouts = workoutService.getWorkouts(pageable, userId);
        return new ResponseEntity<>(workouts, HttpStatus.OK);
    }

    @PostMapping("/{userId}/workouts")
    public ResponseEntity<Workout> createWorkout(@PathVariable("userId") long userId, @RequestBody WorkoutTo workoutTo) {
        if (workoutTo.getId() != null) {
            throw new IllegalRequestDataException(workoutTo + " must be new (id=null)");
        }
        log.info("create {} for user id={}", workoutTo, userId);
        Workout workout = workoutService.createWorkout(workoutTo, userId);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @PutMapping("/{userId}/workouts/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable("userId") long userId, @PathVariable("id") Long id, @RequestBody WorkoutTo workoutTo) {
        if (!id.equals(workoutTo.getId())) {
            throw new IllegalRequestDataException(workoutTo + " must be with id=" + id);
        }
        Workout workout = workoutService.updateWorkout(workoutTo, userId);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/workouts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkout(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        log.info("delete workout id={} for user id={}", id, userId);
        workoutService.deleteWorkout(id, userId);
    }
}
