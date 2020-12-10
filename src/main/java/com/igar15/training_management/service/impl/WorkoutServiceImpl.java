package com.igar15.training_management.service.impl;

import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.exceptions.WorkoutExistException;
import com.igar15.training_management.repository.UserRepository;
import com.igar15.training_management.repository.WorkoutRepository;
import com.igar15.training_management.service.WorkoutService;
import com.igar15.training_management.to.WorkoutTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<Workout> getWorkouts(Pageable pageable, long userId) {
        return workoutRepository.findAllByUser_IdOrderByDateTimeDesc(pageable, userId);
    }

    @Override
    public Workout getWorkoutById(long id, long userId) {
        return workoutRepository.findByIdAndUser_Id(id, userId).orElseThrow(() -> new MyEntityNotFoundException("Not found workout with id: " + id));
    }

    @Override
    @Transactional
    public Workout createWorkout(WorkoutTo workoutTo, long userId) {
        Assert.notNull(workoutTo, "Workout must not be null");
        LocalDateTime dateTime = workoutTo.getDateTime();
        if (workoutRepository.findByDateTimeAndUser_Id(dateTime, userId).isPresent()) {
            throw new WorkoutExistException("Workout with date " + dateTime.toLocalDate() + " and time " + dateTime.toLocalTime() + " already exists");
        }
        Workout workout = new Workout();
        workout.setDateTime(dateTime);
        workout.setUser(userRepository.getOne(userId));
        workoutRepository.save(workout);
        return workout;
    }

    @Override
    @Transactional
    public Workout updateWorkout(WorkoutTo workoutTo, long userId) {
        Assert.notNull(workoutTo, "Workout must not be null");
        LocalDateTime dateTime = workoutTo.getDateTime();
        Optional<Workout> optionalWorkout = workoutRepository.findByDateTimeAndUser_Id(dateTime, userId);
        if (optionalWorkout.isPresent() && !optionalWorkout.get().getId().equals(workoutTo.getId())) {
            throw new WorkoutExistException("Workout with date " + dateTime.toLocalDate() + " and time " + dateTime.toLocalTime() + " already exists");
        }
        Workout workout = getWorkoutById(workoutTo.getId(), userId);
        workout.setDateTime(workoutTo.getDateTime());
        workoutRepository.save(workout);
        return workout;
    }

    @Override
    public void deleteWorkout(long id, long userId) {
        Workout workout = getWorkoutById(id, userId);
        workoutRepository.delete(workout);
    }

}
