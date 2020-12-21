package com.igar15.training_management.utils;

import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.igar15.training_management.testdata.ExerciseTypeTestData.*;
import static com.igar15.training_management.testdata.UserTestData.*;

class JsonUtilTest {

    @Test
    void readWriteValues() {
        String json = JsonUtil.writeValue(List.of(USER1_EXERCISE_TYPE1, USER1_EXERCISE_TYPE2, USER1_EXERCISE_TYPE3));
        List<ExerciseType> exerciseTypes = JsonUtil.readValues(json, ExerciseType.class);
        Assertions.assertThat(exerciseTypes).usingDefaultElementComparator().isEqualTo(List.of(USER1_EXERCISE_TYPE1, USER1_EXERCISE_TYPE2, USER1_EXERCISE_TYPE3));
    }

    @Test
    void readWriteValue() {
        String json = JsonUtil.writeValue(USER1_EXERCISE_TYPE1);
        ExerciseType exerciseType = JsonUtil.readValue(json, ExerciseType.class);
        Assertions.assertThat(exerciseType).usingRecursiveComparison().isEqualTo(USER1_EXERCISE_TYPE1);
    }

    @Test
    void writeOnlyAccess() {
        String json = JsonUtil.writeValue(USER1);
        Assertions.assertThat(json).doesNotContain("password");
    }

    @Test
    void readValuesFromPage() {
        String jsonWithPageAttributes = "{\"content\":[{\"id\":1003,\"name\":\"admin\",\"email\":\"admin@test.ru\",\"enabled\":true,\"isNonLocked\":true,\"role\":\"ROLE_ADMIN\"},{\"id\":1000,\"name\":\"user1\",\"email\":\"user1@test.ru\",\"enabled\":true,\"isNonLocked\":true,\"role\":\"ROLE_USER\"},{\"id\":1001,\"name\":\"user2\",\"email\":\"user2@test.ru\",\"enabled\":false,\"isNonLocked\":true,\"role\":\"ROLE_USER\"},{\"id\":1002,\"name\":\"user3\",\"email\":\"user3@test.ru\",\"enabled\":true,\"isNonLocked\":false,\"role\":\"ROLE_USER\"}],\"pageable\":{\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"offset\":0,\"pageSize\":20,\"pageNumber\":0,\"paged\":true,\"unpaged\":false},\"last\":true,\"totalElements\":4,\"totalPages\":1,\"size\":20,\"number\":0,\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"first\":true,\"numberOfElements\":4,\"empty\":false}";
        List<User> users = JsonUtil.readValuesFromPage(jsonWithPageAttributes, User.class);
        Assertions.assertThat(users).usingDefaultElementComparator().isEqualTo(List.of(ADMIN, USER1, USER2, USER3));
    }
}