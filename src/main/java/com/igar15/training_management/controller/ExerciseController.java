package com.igar15.training_management.controller;

import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/{userId}/workouts/{workoutId}/exercises/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable("userId") long userId, @PathVariable("workoutId") long workoutId, @PathVariable("id") long id) {
        Exercise exercise = exerciseService.getExerciseByIdAndUserId(id, userId);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }
}
