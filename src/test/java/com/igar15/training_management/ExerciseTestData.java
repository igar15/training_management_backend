package com.igar15.training_management;

import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.to.ExerciseTo;

import static com.igar15.training_management.ExerciseTypeTestData.*;
import static com.igar15.training_management.WorkoutTestData.*;

public class ExerciseTestData {

    public static final long NOT_FOUND_EXERCISE_ID = 10;

    public static final long USER1_WORKOUT1_EXERCISE1_ID = 1014;
    public static final long USER1_WORKOUT1_EXERCISE2_ID = 1015;
    public static final long USER1_WORKOUT1_EXERCISE3_ID = 1016;
    public static final long ADMIN_WORKOUT1_EXERCISE1_ID = 1020;

    public static final Exercise USER1_WORKOUT1_EXERCISE1 = new Exercise(USER1_WORKOUT1_EXERCISE1_ID, 90, USER1_EXERCISE_TYPE1);
    public static final Exercise USER1_WORKOUT1_EXERCISE2 = new Exercise(USER1_WORKOUT1_EXERCISE2_ID, 30, USER1_EXERCISE_TYPE2);
    public static final Exercise USER1_WORKOUT1_EXERCISE3 = new Exercise(USER1_WORKOUT1_EXERCISE3_ID, 5, USER1_EXERCISE_TYPE3);

    public static ExerciseTo getNewExerciseTo() {
        return new ExerciseTo(null, 100, USER1_WORKOUT1_ID, USER1_EXERCISE_TYPE1_ID);
    }

    public static Exercise getNewExercise() {
        return new Exercise(null, 100, USER1_EXERCISE_TYPE1);
    }

    public static ExerciseTo getUpdatedExerciseTo() {
        return new ExerciseTo(USER1_WORKOUT1_EXERCISE1_ID, 25, USER1_WORKOUT1_ID, USER1_EXERCISE_TYPE1_ID);
    }

    public static Exercise getUpdatedExercise() {
        return new Exercise(USER1_WORKOUT1_EXERCISE1_ID, 25, USER1_EXERCISE_TYPE1);
    }
}
