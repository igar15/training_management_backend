package com.igar15.training_management.repository;

import com.igar15.training_management.entity.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseTypeRepository extends JpaRepository<ExerciseType, Long> {
}
