package com.igar15.training_management.repository;

import com.igar15.training_management.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findAllByWorkout_IdAndWorkout_User_Id(long workoutId, long userId);

    Optional<Exercise> findByIdAndWorkout_User_Id(long id, long userId);

}
