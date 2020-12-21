package com.igar15.training_management.controller;

import com.igar15.training_management.*;
import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.service.ExerciseService;
import com.igar15.training_management.to.ExerciseTo;
import com.igar15.training_management.to.MyHttpResponse;
import com.igar15.training_management.utils.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.igar15.training_management.testdata.ControllerTestData.*;
import static com.igar15.training_management.testdata.ExerciseTestData.*;
import static com.igar15.training_management.testdata.ExerciseTypeTestData.*;
import static com.igar15.training_management.testdata.UserTestData.*;
import static com.igar15.training_management.testdata.WorkoutTestData.*;
import static org.assertj.core.api.Assertions.*;

class ExerciseControllerTest extends AbstractControllerTest {

    @Autowired
    private ExerciseService exerciseService;

    @Test
    void getExercise() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Exercise exercise = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Exercise.class);
        assertThat(exercise).usingRecursiveComparison().isEqualTo(USER1_WORKOUT1_EXERCISE1);
    }

    @Test
    void getExerciseWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void getExerciseNotOwnWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + ADMIN_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void getExerciseWhenNotOwnWorkoutIdWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI + "/" + ADMIN_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void getExerciseWhenNotOwnUserIdWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + ADMIN_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI + "/" + ADMIN_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void getExerciseNotOwnWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Exercise exercise = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Exercise.class);
        assertThat(exercise).usingRecursiveComparison().isEqualTo(USER1_WORKOUT1_EXERCISE1);
    }

    @Test
    void getExerciseNotFound() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + NOT_FOUND_EXERCISE_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_NOT_FOUND_RESPONSE);
    }

    @Test
    void getAllExercises() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<Exercise> exercises = JsonUtil.readValues(resultActions.andReturn().getResponse().getContentAsString(), Exercise.class);
        assertThat(exercises).usingRecursiveComparison().isEqualTo(List.of(USER1_WORKOUT1_EXERCISE1, USER1_WORKOUT1_EXERCISE2, USER1_WORKOUT1_EXERCISE3));
    }

    @Test
    void getAllExercisesWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void getAllExercisesWhenWorkoutNotOwnAndUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void getAllExercisesWhenNotOwnUserIdAndUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + ADMIN_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void getAllExercisesNotOwnWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<Exercise> exercises = JsonUtil.readValues(resultActions.andReturn().getResponse().getContentAsString(), Exercise.class);
        assertThat(exercises).usingRecursiveComparison().isEqualTo(List.of(USER1_WORKOUT1_EXERCISE1, USER1_WORKOUT1_EXERCISE2, USER1_WORKOUT1_EXERCISE3));
    }

    @Test
    void createExercise() throws Exception {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        Exercise newExercise = getNewExercise();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Exercise createdExercise = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Exercise.class);
        Long newId = createdExercise.getId();
        newExercise.setId(newId);
        assertThat(createdExercise).usingRecursiveComparison().isEqualTo(newExercise);
    }

    @Test
    void createExerciseWhenUnAuth() throws Exception {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void createExerciseWhenWorkoutIdInUrlNotOwnAndUserTry() throws Exception {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        newExerciseTo.setWorkoutId(ADMIN_WORKOUT1_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void createExerciseWhenUserIdNotOwnAndUserTry() throws Exception {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + ADMIN_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void createExerciseWhenUserIdNotOwnAndWorkoutIdNotOwnAndAdminTry() throws Exception {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        Exercise newExercise = getNewExercise();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI)
                .headers(adminJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Exercise createdExercise = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Exercise.class);
        Long newId = createdExercise.getId();
        newExercise.setId(newId);
        assertThat(createdExercise).usingRecursiveComparison().isEqualTo(newExercise);
    }

    @Test
    void createExerciseWithNotValidAttributes() throws Exception {
        ExerciseTo newExerciseTo = getNewExerciseTo();
        newExerciseTo.setQuantity(0);
        ResultActions resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI, userJwtHeader);
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_EXERCISE_QUANTITY_MIN_RESPONSE);

        newExerciseTo = getNewExerciseTo();
        newExerciseTo.setExerciseTypeId(null);
        resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI, userJwtHeader);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_EXERCISE_EXERCISE_TYPE_ID_NULL_RESPONSE);

        newExerciseTo = getNewExerciseTo();
        newExerciseTo.setWorkoutId(null);
        resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI, userJwtHeader);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_EXERCISE_WORKOUT_ID_NULL_RESPONSE);

        newExerciseTo = getNewExerciseTo();
        newExerciseTo.setWorkoutId(ADMIN_WORKOUT1_ID);
        resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI, userJwtHeader);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_MUST_BE_WITH_WORKOUT_ID_RESPONSE);

        newExerciseTo = getNewExerciseTo();
        newExerciseTo.setExerciseTypeId(ADMIN_EXERCISE_TYPE1_ID);
        resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI, userJwtHeader);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_TYPE_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void updateExercise() throws Exception {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        Exercise updatedExerciseExpected = getUpdatedExercise();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Exercise exercise = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Exercise.class);
        assertThat(exercise).usingRecursiveComparison().isEqualTo(updatedExerciseExpected);
    }

    @Test
    void updateExerciseWhenUnAuth() throws Exception {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void updateExerciseNotOwnWhenUserTry() throws Exception {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        updatedExerciseTo.setId(ADMIN_WORKOUT1_EXERCISE1_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + ADMIN_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void updateExerciseWhenNotOwnWorkoutIdInUrl() throws Exception {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        updatedExerciseTo.setWorkoutId(ADMIN_WORKOUT1_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void updateExerciseWithNotOwnWorkoutIdInTo() throws Exception {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        updatedExerciseTo.setWorkoutId(NOT_FOUND_WORKOUT_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_UPDATED_MUST_BE_WITH_WORKOUT_ID_RESPONSE);
    }

    @Test
    void updateExerciseWhenIdsNotTheSame() throws Exception {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        updatedExerciseTo.setId(NOT_FOUND_EXERCISE_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_MUST_BE_WITH_ID_RESPONSE);
    }

    @Test
    void updateExerciseNotOwnWhenAdminTry() throws Exception {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        Exercise updatedExerciseExpected = getUpdatedExercise();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(adminJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Exercise exercise = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Exercise.class);
        assertThat(exercise).usingRecursiveComparison().isEqualTo(updatedExerciseExpected);
    }

    @Test
    void updateExerciseNotFound() throws Exception {
        ExerciseTo updatedExerciseTo = getUpdatedExerciseTo();
        updatedExerciseTo.setId(NOT_FOUND_EXERCISE_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + NOT_FOUND_EXERCISE_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedExerciseTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_NOT_FOUND_RESPONSE);
    }

    @Test
    void deleteExercise() throws Exception {
        perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.getExerciseByIdAndWorkoutIdAndUserId(USER1_WORKOUT1_EXERCISE1_ID, USER1_WORKOUT1_ID, USER1_ID));
    }

    @Test
    void deleteExerciseWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void deleteExerciseNotOwnWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + ADMIN_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void deleteExerciseNotOwnWhenAdminTry() throws Exception {
        perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> exerciseService.getExerciseByIdAndWorkoutIdAndUserId(USER1_WORKOUT1_EXERCISE1_ID, USER1_WORKOUT1_ID, USER1_ID));
    }

    @Test
    void deleteExerciseNotOwnWorkoutId() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void deleteExerciseNotOwnUserId() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + ADMIN_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

}