package com.igar15.training_management.controller;

import com.igar15.training_management.*;
import com.igar15.training_management.entity.Exercise;
import com.igar15.training_management.to.ExerciseTo;
import com.igar15.training_management.to.MyHttpResponse;
import com.igar15.training_management.utils.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.igar15.training_management.ControllerTestData.*;
import static com.igar15.training_management.ExerciseTestData.*;
import static com.igar15.training_management.ExerciseTypeTestData.*;
import static com.igar15.training_management.UserTestData.*;
import static com.igar15.training_management.WorkoutTestData.*;
import static org.assertj.core.api.Assertions.*;

class ExerciseControllerTest extends AbstractControllerTest {

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
    void getExerciseNotOwnWithOwnUserIdAndOwnWorkoutIdWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI + "/" + ADMIN_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void getExerciseNotOwnWithOwnUserIdAndNotOwnWorkoutIdWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI + "/" + ADMIN_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void getExerciseNotOwnWithNotOwnUserIdAndNotOwnWorkoutIdWhenUserTry() throws Exception {
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
    void getExerciseNotFoundForDifferentWorkout() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT2_ID + EXERCISES_URI + "/" + USER1_WORKOUT1_EXERCISE1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_NOT_FOUND_FOR_DIFFERENT_WORKOUT_RESPONSE);
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
    void getAllExercisesWhenWorkoutNotOwnAndOwnUserIdAndUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID + EXERCISES_URI)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void getAllExercisesWhenWorkoutNotOwnAndNotOwnUserIdAndUserTry() throws Exception {
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
    void createExerciseWhenWorkoutIdInUrlNotOwnAndOwnUserIdAndUserTry() throws Exception {
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
        ResultActions resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI);
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_EXERCISE_QUANTITY_MIN_RESPONSE);

        newExerciseTo = getNewExerciseTo();
        newExerciseTo.setExerciseTypeId(null);
        resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_EXERCISE_EXERCISE_TYPE_ID_NULL_RESPONSE);

        newExerciseTo = getNewExerciseTo();
        newExerciseTo.setWorkoutId(null);
        resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_EXERCISE_WORKOUT_ID_NULL_RESPONSE);

        newExerciseTo = getNewExerciseTo();
        newExerciseTo.setWorkoutId(ADMIN_WORKOUT1_ID);
        resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_MUST_BE_WITH_WORKOUT_ID_RESPONSE);

        newExerciseTo = getNewExerciseTo();
        newExerciseTo.setExerciseTypeId(ADMIN_EXERCISE_TYPE1_ID);
        resultActions = getResultActionsWhenToNotValid(newExerciseTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID + EXERCISES_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EXERCISE_TYPE_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void updateExercise() {
    }

    @Test
    void deleteExercise() {
    }

    private ResultActions getResultActionsWhenToNotValid(Object to, String urlTemplate) throws Exception {
        return perform(MockMvcRequestBuilders.post(urlTemplate)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(to)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}