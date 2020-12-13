package com.igar15.training_management.controller;

import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.service.WorkoutService;
import com.igar15.training_management.to.WorkoutTo;
import com.igar15.training_management.to.swaggerTo.SwaggerWorkoutCreateTo;
import com.igar15.training_management.utils.ValidationUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class WorkoutController {

    private final Logger log = LoggerFactory.getLogger(WorkoutController.class);

    @Autowired
    private WorkoutService workoutService;

    @GetMapping("/{userId}/workouts/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${workoutController.GetWorkout.ApiOperation.Value}", notes = "${workoutController.GetWorkout.ApiOperation.Notes}")
    public ResponseEntity<Workout> getWorkout(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        log.info("get workout id={} for user id={}", id, userId);
        Workout workout = workoutService.getWorkoutById(id, userId);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @GetMapping("/{userId}/workouts")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${workoutController.GetWorkouts.ApiOperation.Value}", notes = "${workoutController.GetWorkouts.ApiOperation.Notes}")
    public ResponseEntity<Page<Workout>> getWorkouts(@PathVariable("userId") long userId, Pageable pageable) {
        log.info("get all workouts with pagination=( {} ) for user id={}", pageable, userId);
        Page<Workout> workouts = workoutService.getWorkouts(pageable, userId);
        return new ResponseEntity<>(workouts, HttpStatus.OK);
    }

    @PostMapping("/{userId}/workouts")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${workoutController.CreateWorkout.ApiOperation.Value}", notes = "${workoutController.CreateWorkout.ApiOperation.Notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "workoutTo",
                    value = "Json object for creating a new Workout",
                    dataTypeClass = SwaggerWorkoutCreateTo.class
            )
    })
    public ResponseEntity<Workout> createWorkout(@PathVariable("userId") long userId, @Valid @RequestBody WorkoutTo workoutTo, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult);
        ValidationUtil.checkOnNew(workoutTo);
        log.info("create {} for user id={}", workoutTo, userId);
        Workout workout = workoutService.createWorkout(workoutTo, userId);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @PutMapping("/{userId}/workouts/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${workoutController.UpdateWorkout.ApiOperation.Value}", notes = "${workoutController.UpdateWorkout.ApiOperation.Notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "workoutTo",
                    value = "Json object for updating Workout"
            )
    })
    public ResponseEntity<Workout> updateWorkout(@PathVariable("userId") long userId, @PathVariable("id") Long id, @Valid @RequestBody WorkoutTo workoutTo, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult);
        ValidationUtil.checkIdTheSame(workoutTo, id);
        log.info("update {} for user id={}", workoutTo, userId);
        Workout workout = workoutService.updateWorkout(workoutTo, userId);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/workouts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${workoutController.DeleteWorkout.ApiOperation.Value}", notes = "${workoutController.DeleteWorkout.ApiOperation.Notes}")
    public void deleteWorkout(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        log.info("delete workout id={} for user id={}", id, userId);
        workoutService.deleteWorkout(id, userId);
    }
}
