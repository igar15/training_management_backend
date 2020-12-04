package com.igar15.training_management;

import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.to.WorkoutTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WorkoutTestData {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final long NOT_FOUND_WORKOUT_ID = 10;

    public static final long USER1_WORKOUT1_ID = 1009;
    public static final long USER1_WORKOUT2_ID = 1010;
    public static final long USER1_WORKOUT3_ID = 1011;
    public static final long ADMIN_WORKOUT1_ID = 1012;

    public static final Workout USER1_WORKOUT1 = new Workout(USER1_WORKOUT1_ID, LocalDateTime.parse("2020-11-28 13:00:00", DATE_TIME_FORMATTER));
    public static final Workout USER1_WORKOUT2 = new Workout(USER1_WORKOUT2_ID, LocalDateTime.parse("2020-11-28 18:00:00", DATE_TIME_FORMATTER));
    public static final Workout USER1_WORKOUT3 = new Workout(USER1_WORKOUT3_ID, LocalDateTime.parse("2020-11-29 11:00:00", DATE_TIME_FORMATTER));

    public static final Pageable PAGEABLE = PageRequest.of(0, 10);

    public static final Page<Workout> PAGE = new PageImpl<>(List.of(USER1_WORKOUT3, USER1_WORKOUT2, USER1_WORKOUT1), PAGEABLE, 3);

    public static WorkoutTo getNewWorkoutTo() {
        return new WorkoutTo(null, LocalDateTime.parse("2020-12-03 18:20:00", DATE_TIME_FORMATTER));
    }

    public static Workout getNewWorkout() {
        return new Workout(null, LocalDateTime.parse("2020-12-03 18:20:00", DATE_TIME_FORMATTER));
    }

    public static WorkoutTo getUpdatedWorkoutTo() {
        return new WorkoutTo(USER1_WORKOUT1_ID, LocalDateTime.parse("2020-12-04 13:40:00", DATE_TIME_FORMATTER));
    }

    public static Workout getUpdatedWorkout() {
        return new Workout(USER1_WORKOUT1_ID, LocalDateTime.parse("2020-12-04 13:40:00", DATE_TIME_FORMATTER));
    }

}
