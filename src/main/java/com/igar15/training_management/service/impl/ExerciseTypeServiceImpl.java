package com.igar15.training_management.service.impl;

import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.entity.enums.Measure;
import com.igar15.training_management.exceptions.ExerciseTypeExistException;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.repository.ExerciseTypeRepository;
import com.igar15.training_management.repository.UserRepository;
import com.igar15.training_management.service.ExerciseTypeService;
import com.igar15.training_management.to.ExerciseTypeTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseTypeServiceImpl implements ExerciseTypeService {

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ExerciseType> getExercisesTypes(long userId) {
        return exerciseTypeRepository.findAllByUser_Id(userId);
    }

    @Override
    public ExerciseType getExerciseTypeById(long id, long userId) {
        return exerciseTypeRepository.findByIdAndUser_Id(id, userId).orElseThrow(() -> new MyEntityNotFoundException("Not found exercise type with id: " + id));
    }

    @Override
    public ExerciseType createExerciseType(ExerciseTypeTo exerciseTypeTo, long userId) {
        Assert.notNull(exerciseTypeTo, "Exercise type must not be null");
        String name = exerciseTypeTo.getName();
        if (exerciseTypeRepository.findByNameAndUser_Id(name, userId).isPresent()) {
            throw new ExerciseTypeExistException("Exercise type with name " + name + " already exists");
        }
        ExerciseType exerciseType = new ExerciseType();
        exerciseType.setName(name);
        if (exerciseTypeTo.getMeasure() != null) {
            exerciseType.setMeasure(Measure.valueOf(exerciseTypeTo.getMeasure().toUpperCase()));
        }
        exerciseType.setUser(userRepository.getOne(userId));
        exerciseTypeRepository.save(exerciseType);
        return exerciseType;
    }

    @Override
    public ExerciseType updateExerciseType(ExerciseTypeTo exerciseTypeTo, long userId) {
        Assert.notNull(exerciseTypeTo, "Exercise type must not be null");
        String name = exerciseTypeTo.getName();
        Optional<ExerciseType> exerciseTypeOptional = exerciseTypeRepository.findByNameAndUser_Id(name, userId);
        if (exerciseTypeOptional.isPresent() && !exerciseTypeOptional.get().getId().equals(exerciseTypeTo.getId())) {
            throw new ExerciseTypeExistException("Exercise type with name " + name + " already exists");
        }
        ExerciseType exerciseType = getExerciseTypeById(exerciseTypeTo.getId(), userId);
        exerciseType.setName(exerciseTypeTo.getName());
        if (exerciseTypeTo.getMeasure() != null) {
            exerciseType.setMeasure(Measure.valueOf(exerciseTypeTo.getMeasure().toUpperCase()));
        }
        exerciseTypeRepository.save(exerciseType);
        return exerciseType;
    }

    @Override
    public void deleteExerciseType(long id, long userId) {
        ExerciseType exerciseType = getExerciseTypeById(id, userId);
        exerciseTypeRepository.delete(exerciseType);
    }
}