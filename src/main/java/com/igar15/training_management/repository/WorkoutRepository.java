package com.igar15.training_management.repository;

import com.igar15.training_management.entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    Page<Workout> findAllByUser_Id(Pageable pageable, long userId);

    Optional<Workout> findByIdAndUser_Id(long id, long userId);

    Optional<Workout> findByDateTimeAndUser_Id(LocalDateTime dateTime, long userId);
}
