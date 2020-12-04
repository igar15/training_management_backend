package com.igar15.training_management;

import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.entity.enums.Measure;
import com.igar15.training_management.to.ExerciseTypeTo;

public class ExerciseTypeTestData {

    public static final long NOT_FOUND_EXERCISE_TYPE_ID = 10;

    public static final long USER1_EXERCISE_TYPE1_ID = 1004;
    public static final long USER1_EXERCISE_TYPE2_ID = 1005;
    public static final long USER1_EXERCISE_TYPE3_ID = 1006;
    public static final long ADMIN_EXERCISE_TYPE1_ID = 1007;

    public static final ExerciseType USER1_EXERCISE_TYPE1 = new ExerciseType(USER1_EXERCISE_TYPE1_ID, "user1 exercise type 1", Measure.TIMES);
    public static final ExerciseType USER1_EXERCISE_TYPE2 = new ExerciseType(USER1_EXERCISE_TYPE2_ID, "user1 exercise type 2", Measure.SECONDS);
    public static final ExerciseType USER1_EXERCISE_TYPE3 = new ExerciseType(USER1_EXERCISE_TYPE3_ID, "user1 exercise type 3", Measure.KILOMETERS);

    public static ExerciseTypeTo getNewExerciseTypeTo() {
        return new ExerciseTypeTo(null, "new exercise type", Measure.TIMES.toString());
    }

    public static ExerciseType getNewExerciseType() {
        return new ExerciseType(null, "new exercise type", Measure.TIMES);
    }

    public static ExerciseTypeTo getUpdatedExerciseTypeTo() {
        return new ExerciseTypeTo(USER1_EXERCISE_TYPE1_ID, "user1 exercise type 1 updated", Measure.KILOMETERS.toString());
    }

    public static ExerciseType getUpdatedExerciseType() {
        return new ExerciseType(USER1_EXERCISE_TYPE1_ID, "user1 exercise type 1 updated", Measure.KILOMETERS);
    }
}
