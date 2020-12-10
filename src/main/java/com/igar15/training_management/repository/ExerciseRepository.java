package com.igar15.training_management.repository;

import com.igar15.training_management.entity.Exercise;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @EntityGraph(attributePaths = "exerciseType")
    List<Exercise> findAllByWorkout_IdAndUser_Id(long workoutId, long userId);

    @EntityGraph(attributePaths = "exerciseType")
    Optional<Exercise> findByIdAndUser_Id(long id, long userId);

}
