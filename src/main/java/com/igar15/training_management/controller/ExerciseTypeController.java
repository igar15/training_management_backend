package com.igar15.training_management.controller;

import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.service.ExerciseTypeService;
import com.igar15.training_management.to.ExerciseTypeTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class ExerciseTypeController {

    @Autowired
    private ExerciseTypeService exerciseTypeService;

    @GetMapping("/{userId}/exerciseTypes/{id}")
    public ResponseEntity<ExerciseType> getExerciseType(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        ExerciseType exerciseType = exerciseTypeService.getExerciseTypeById(id, userId);
        return new ResponseEntity<>(exerciseType, HttpStatus.OK);
    }

    @GetMapping("/{userId}/exerciseTypes")
    public ResponseEntity<List<ExerciseType>> getExerciseTypes(@PathVariable("userId") long userId) {
        List<ExerciseType> exercisesTypes = exerciseTypeService.getExercisesTypes(userId);
        return new ResponseEntity<>(exercisesTypes, HttpStatus.OK);
    }

    @PostMapping("/{userId}/exerciseTypes")
    public ResponseEntity<ExerciseType> createExerciseType(@PathVariable("userId") long userId, @RequestBody ExerciseTypeTo exerciseTypeTo) {
        ExerciseType exerciseType = exerciseTypeService.createExerciseType(exerciseTypeTo, userId);
        return new ResponseEntity<>(exerciseType, HttpStatus.OK);
    }

    @PutMapping("/{userId}/exerciseTypes/{id}")
    public ResponseEntity<ExerciseType> updateExerciseType(@PathVariable("userId") long userId, @PathVariable("id") Long id, @RequestBody ExerciseTypeTo exerciseTypeTo) {
        if (!id.equals(exerciseTypeTo.getId())) {
            throw new IllegalArgumentException("ExerciseType must be with id: " + id);
        }
        ExerciseType exerciseType = exerciseTypeService.updateExerciseType(exerciseTypeTo, userId);
        return new ResponseEntity<>(exerciseType, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/exerciseTypes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExerciseType(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        exerciseTypeService.deleteExerciseType(id, userId);
    }



}
