package com.igar15.training_management.service;

import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.to.ExerciseTypeTo;

import java.util.List;

public interface ExerciseTypeService {

    List<ExerciseType> getExercisesTypes(long userId);

    ExerciseType getExerciseTypeById(long id, long userId);

    ExerciseType createExerciseType(ExerciseTypeTo exerciseTypeTo, long userId);

    ExerciseType updateExerciseType(ExerciseTypeTo exerciseTypeTo, long userId);

    void deleteExerciseType(long id, long userId);

}
