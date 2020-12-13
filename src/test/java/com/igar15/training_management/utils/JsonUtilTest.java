package com.igar15.training_management.utils;

import com.igar15.training_management.ExerciseTypeTestData;
import com.igar15.training_management.UserTestData;
import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.igar15.training_management.ExerciseTypeTestData.*;

@SpringBootTest
class JsonUtilTest {

    @Autowired
    private JsonUtil jsonUtil;

    @Test
    void readWriteValues() {
        String json = jsonUtil.writeValue(List.of(USER1_EXERCISE_TYPE1, USER1_EXERCISE_TYPE2, USER1_EXERCISE_TYPE3));
        List<ExerciseType> exerciseTypes = jsonUtil.readValues(json, ExerciseType.class);
        Assertions.assertThat(exerciseTypes).usingDefaultElementComparator().isEqualTo(List.of(USER1_EXERCISE_TYPE1, USER1_EXERCISE_TYPE2, USER1_EXERCISE_TYPE3));
    }

    @Test
    void readWriteValue() {
        String json = jsonUtil.writeValue(USER1_EXERCISE_TYPE1);
        ExerciseType exerciseType = jsonUtil.readValue(json, ExerciseType.class);
        Assertions.assertThat(exerciseType).usingRecursiveComparison().isEqualTo(USER1_EXERCISE_TYPE1);
    }

    @Test
    void writeOnlyAccess() {
        String json = jsonUtil.writeValue(UserTestData.USER1);
        Assertions.assertThat(json).doesNotContain("password");
    }
}