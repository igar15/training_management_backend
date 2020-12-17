package com.igar15.training_management.controller;

import com.igar15.training_management.AbstractControllerTest;
import com.igar15.training_management.ControllerTestData;
import com.igar15.training_management.ExerciseTypeTestData;
import com.igar15.training_management.UserTestData;
import com.igar15.training_management.entity.ExerciseType;
import com.igar15.training_management.to.ExerciseTypeTo;
import com.igar15.training_management.to.MyHttpResponse;
import com.igar15.training_management.utils.JsonUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.igar15.training_management.ControllerTestData.*;
import static com.igar15.training_management.ExerciseTypeTestData.*;
import static com.igar15.training_management.UserTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ExerciseTypeControllerTest extends AbstractControllerTest {



    @Test
    void getExerciseType() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI + "/" + USER1_EXERCISE_TYPE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        ExerciseType exerciseType = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), ExerciseType.class);
        assertThat(exerciseType).usingRecursiveComparison().isEqualTo(USER1_EXERCISE_TYPE1);
    }

    @Test
    void getExerciseTypeWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI + "/" + USER1_EXERCISE_TYPE1_ID))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void getExerciseTypeNotOwnWithOwnUserIdWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI + "/" + ADMIN_EXERCISE_TYPE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_TYPE_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void getExerciseTypeNotOwnWithNotOwnUserIdWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + ADMIN_ID + EXERCISE_TYPES_URI + "/" + ADMIN_EXERCISE_TYPE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void getExerciseTypeNotOwnWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI + "/" + USER1_EXERCISE_TYPE1_ID)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        ExerciseType exerciseType = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), ExerciseType.class);
        assertThat(exerciseType).usingRecursiveComparison().isEqualTo(USER1_EXERCISE_TYPE1);
    }

    @Test
    void getExerciseTypeNotFound() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI + "/" + NOT_FOUND_EXERCISE_TYPE_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_TYPE_NOT_FOUND_RESPONSE);
    }

    @Test
    void getAllExerciseTypes() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<ExerciseType> exerciseTypes = JsonUtil.readValues(resultActions.andReturn().getResponse().getContentAsString(), ExerciseType.class);
        assertThat(exerciseTypes).usingDefaultElementComparator().isEqualTo(List.of(USER1_EXERCISE_TYPE1, USER1_EXERCISE_TYPE2, USER1_EXERCISE_TYPE3));
    }

    @Test
    void getAllExerciseTypesWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void getAllExerciseTypesNotOwnWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER2_ID + EXERCISE_TYPES_URI)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void getAllExerciseTypesNotOwnWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<ExerciseType> exerciseTypes = JsonUtil.readValues(resultActions.andReturn().getResponse().getContentAsString(), ExerciseType.class);
        assertThat(exerciseTypes).usingDefaultElementComparator().isEqualTo(List.of(USER1_EXERCISE_TYPE1, USER1_EXERCISE_TYPE2, USER1_EXERCISE_TYPE3));
    }

    @Test
    void createExerciseType() throws Exception {
        ExerciseTypeTo newExerciseTypeTo = getNewExerciseTypeTo();
        ExerciseType newExerciseType = getNewExerciseType();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newExerciseTypeTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        ExerciseType exerciseType = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), ExerciseType.class);
        Long newId = exerciseType.getId();
        newExerciseType.setId(newId);
        assertThat(exerciseType).usingRecursiveComparison().isEqualTo(newExerciseType);
    }

    @Test
    void createExerciseTypeNotOwnWhenUserTry() throws Exception {
        ExerciseTypeTo newExerciseTypeTo = getNewExerciseTypeTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + ADMIN_ID + EXERCISE_TYPES_URI)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newExerciseTypeTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void createExerciseTypeNotOwnWhenAdminTry() throws Exception {
        ExerciseTypeTo newExerciseTypeTo = getNewExerciseTypeTo();
        ExerciseType newExerciseType = getNewExerciseType();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI)
                .headers(adminJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newExerciseTypeTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        ExerciseType exerciseType = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), ExerciseType.class);
        Long newId = exerciseType.getId();
        newExerciseType.setId(newId);
        assertThat(exerciseType).usingRecursiveComparison().isEqualTo(newExerciseType);
    }

    @Test
    void createExerciseTypeWithNotValidAttributes() throws Exception {
        ExerciseTypeTo newExerciseTypeTo = getNewExerciseTypeTo();
        newExerciseTypeTo.setName(USER1_EXERCISE_TYPE1.getName());
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + EXERCISE_TYPES_URI)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newExerciseTypeTo)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_TYPE_ALREADY_EXIST_RESPONSE);
    }

    @Test
    void updateExerciseType() {
    }

    @Test
    void deleteExerciseType() {
    }
}