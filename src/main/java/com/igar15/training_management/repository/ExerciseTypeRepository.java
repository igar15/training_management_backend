package com.igar15.training_management.repository;

import com.igar15.training_management.entity.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseTypeRepository extends JpaRepository<ExerciseType, Long> {

    List<ExerciseType> findAllByUser_Id(long userId);

    Optional<ExerciseType> findByIdAndUser_Id(long id, long userId);

    Optional<ExerciseType> findByNameAndUser_Id(String name, long userId);


}
