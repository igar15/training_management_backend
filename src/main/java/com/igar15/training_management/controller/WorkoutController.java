package com.igar15.training_management.controller;

import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.service.WorkoutService;
import com.igar15.training_management.to.WorkoutTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @GetMapping("/{userId}/workouts/{id}")
    public ResponseEntity<Workout> getWorkout(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        Workout workout = workoutService.getWorkoutById(id, userId);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @GetMapping("/{userId}/workouts")
    public ResponseEntity<Page<Workout>> getWorkouts(@PathVariable("userId") long userId, Pageable pageable) {
        Page<Workout> workouts = workoutService.getWorkouts(pageable, userId);
        return new ResponseEntity<>(workouts, HttpStatus.OK);
    }

    @PostMapping("/{userId}/workouts")
    public ResponseEntity<Workout> createWorkout(@PathVariable("userId") long userId, @RequestBody WorkoutTo workoutTo) {
        Workout workout = workoutService.createWorkout(workoutTo, userId);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @PutMapping("/{userId}/workouts/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable("userId") long userId, @PathVariable("id") Long id, @RequestBody WorkoutTo workoutTo) {
        if (!id.equals(workoutTo.getId())) {
            throw new IllegalArgumentException("Workout must be with id: " + id);
        }
        Workout workout = workoutService.updateWorkout(workoutTo, userId);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/workouts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkout(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        workoutService.deleteWorkout(id, userId);
    }
}
