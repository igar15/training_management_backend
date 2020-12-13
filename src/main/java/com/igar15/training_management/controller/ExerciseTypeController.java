package com.igar15.training_management.controller;

import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.service.ExerciseTypeService;
import com.igar15.training_management.to.ExerciseTypeTo;
import com.igar15.training_management.to.swaggerTo.SwaggerExerciseTypeCreateTo;
import com.igar15.training_management.utils.ValidationUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class ExerciseTypeController {

    private final Logger log = LoggerFactory.getLogger(ExerciseTypeController.class);

    @Autowired
    private ExerciseTypeService exerciseTypeService;

    @GetMapping("/{userId}/exerciseTypes/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${exerciseTypeController.GetExerciseType.ApiOperation.Value}", notes = "${exerciseTypeController.GetExerciseType.ApiOperation.Notes}")
    public ResponseEntity<ExerciseType> getExerciseType(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        log.info("get exercise type id={} for user id={}", id, userId);
        ExerciseType exerciseType = exerciseTypeService.getExerciseTypeById(id, userId);
        return new ResponseEntity<>(exerciseType, HttpStatus.OK);
    }

    @GetMapping("/{userId}/exerciseTypes")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${exerciseTypeController.GetExerciseTypes.ApiOperation.Value}", notes = "${exerciseTypeController.GetExerciseTypes.ApiOperation.Notes}")
    public ResponseEntity<List<ExerciseType>> getExerciseTypes(@PathVariable("userId") long userId) {
        log.info("get all exercise types for user with id={}", userId);
        List<ExerciseType> exercisesTypes = exerciseTypeService.getExercisesTypes(userId);
        return new ResponseEntity<>(exercisesTypes, HttpStatus.OK);
    }

    @PostMapping("/{userId}/exerciseTypes")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${exerciseTypeController.CreateExerciseType.ApiOperation.Value}", notes = "${exerciseTypeController.CreateExerciseType.ApiOperation.Notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "exerciseTypeTo",
                    value = "Json object for creating a new Exercise type",
                    dataTypeClass = SwaggerExerciseTypeCreateTo.class
            )
    })
    public ResponseEntity<ExerciseType> createExerciseType(@PathVariable("userId") long userId, @Valid @RequestBody ExerciseTypeTo exerciseTypeTo, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult);
        ValidationUtil.checkOnNew(exerciseTypeTo);
        log.info("create {} for user id={}", exerciseTypeTo, userId);
        ExerciseType exerciseType = exerciseTypeService.createExerciseType(exerciseTypeTo, userId);
        return new ResponseEntity<>(exerciseType, HttpStatus.OK);
    }

    @PutMapping("/{userId}/exerciseTypes/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${exerciseTypeController.UpdateExerciseType.ApiOperation.Value}", notes = "${exerciseTypeController.UpdateExerciseType.ApiOperation.Notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "exerciseTypeTo",
                    value = "Json object for updating Exercise type"
            )
    })
    public ResponseEntity<ExerciseType> updateExerciseType(@PathVariable("userId") long userId, @PathVariable("id") Long id, @Valid @RequestBody ExerciseTypeTo exerciseTypeTo, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult);
        ValidationUtil.checkIdTheSame(exerciseTypeTo, id);
        log.info("update {} for user id={}", exerciseTypeTo, userId);
        ExerciseType exerciseType = exerciseTypeService.updateExerciseType(exerciseTypeTo, userId);
        return new ResponseEntity<>(exerciseType, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/exerciseTypes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ApiOperation(value = "${exerciseTypeController.DeleteExerciseType.ApiOperation.Value}", notes = "${exerciseTypeController.DeleteExerciseType.ApiOperation.Notes}")
    public void deleteExerciseType(@PathVariable("userId") long userId, @PathVariable("id") long id) {
        log.info("delete exercise type id={} for user id={}", id, userId);
        exerciseTypeService.deleteExerciseType(id, userId);
    }

}
