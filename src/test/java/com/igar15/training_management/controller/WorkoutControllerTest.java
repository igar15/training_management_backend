package com.igar15.training_management.controller;

import com.igar15.training_management.AbstractControllerTest;
import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.service.WorkoutService;
import com.igar15.training_management.to.MyHttpResponse;
import com.igar15.training_management.to.WorkoutTo;
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
import static com.igar15.training_management.testdata.UserTestData.*;
import static com.igar15.training_management.testdata.WorkoutTestData.*;
import static org.assertj.core.api.Assertions.*;

class WorkoutControllerTest extends AbstractControllerTest {

    @Autowired
    private WorkoutService workoutService;

    @Test
    void getWorkout() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Workout workout = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        assertThat(workout).usingRecursiveComparison().isEqualTo(USER1_WORKOUT1);
    }

    @Test
    void getWorkoutWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void getWorkoutNotOwnWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void getWorkoutWithNotOwnUserIdWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + ADMIN_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void getWorkoutNotOwnWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Workout workout = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        assertThat(workout).usingRecursiveComparison().isEqualTo(USER1_WORKOUT1);
    }

    @Test
    void getWorkoutNotFound() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + NOT_FOUND_WORKOUT_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_FOUND_RESPONSE);
    }

    @Test
    void getAllWorkouts() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<Workout> workouts = JsonUtil.readValuesFromPage(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        assertThat(workouts).usingRecursiveComparison().isEqualTo(List.of(USER1_WORKOUT3, USER1_WORKOUT2, USER1_WORKOUT1));
    }

    @Test
    void getAllWorkoutsWithPaginationSecondPageSizeTwo() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "?page=1&size=2")
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<Workout> workouts = JsonUtil.readValuesFromPage(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        assertThat(workouts).usingRecursiveComparison().isEqualTo(List.of(USER1_WORKOUT1));
    }

    @Test
    void getAllWorkoutsWithPaginationSecondPageSizeTwoSortedByDateTimeAsc() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "?page=1&size=2&sort=dateTime")
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<Workout> workouts = JsonUtil.readValuesFromPage(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        assertThat(workouts).usingRecursiveComparison().isEqualTo(List.of(USER1_WORKOUT3));
    }

    @Test
    void getAllWorkoutsWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void getAllWorkoutsNotOwnWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + ADMIN_ID + WORKOUTS_URI)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void getAllWorkoutsNotOwnWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID + WORKOUTS_URI)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<Workout> workouts = JsonUtil.readValuesFromPage(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        assertThat(workouts).usingRecursiveComparison().isEqualTo(List.of(USER1_WORKOUT3, USER1_WORKOUT2, USER1_WORKOUT1));
    }

    @Test
    void createWorkout() throws Exception {
        WorkoutTo newWorkoutTo = getNewWorkoutTo();
        Workout newWorkout = getNewWorkout();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + WORKOUTS_URI)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Workout createdWorkout = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        Long newId = createdWorkout.getId();
        newWorkout.setId(newId);
        assertThat(createdWorkout).usingRecursiveComparison().isEqualTo(newWorkout);
    }

    @Test
    void createWorkoutWhenUnAuth() throws Exception {
        WorkoutTo newWorkoutTo = getNewWorkoutTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + WORKOUTS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void createWorkoutNotOwnWhenUserTry() throws Exception {
        WorkoutTo newWorkoutTo = getNewWorkoutTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + ADMIN_ID + WORKOUTS_URI)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void createWorkoutNotOwnWhenAdminTry() throws Exception {
        WorkoutTo newWorkoutTo = getNewWorkoutTo();
        Workout newWorkout = getNewWorkout();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + WORKOUTS_URI)
                .headers(adminJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Workout createdWorkout = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        Long newId = createdWorkout.getId();
        newWorkout.setId(newId);
        assertThat(createdWorkout).usingRecursiveComparison().isEqualTo(newWorkout);
    }

    @Test
    void createWorkoutWithNotValidAttributes() throws Exception {
        WorkoutTo newWorkoutTo = getNewWorkoutTo();
        newWorkoutTo.setDateTime(USER1_WORKOUT1.getDateTime());
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/" + USER1_ID + WORKOUTS_URI)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_ALREADY_EXIST_RESPONSE);

        newWorkoutTo.setDateTime(null);
        resultActions = getResultActionsWhenToNotValid(newWorkoutTo, USERS_URI + "/" + USER1_ID + WORKOUTS_URI, userJwtHeader);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_WORKOUT_DATETIME_NULL_RESPONSE);
    }

    @Test
    void updateWorkout() throws Exception {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        Workout updatedWorkoutExpected = getUpdatedWorkout();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Workout workout = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        assertThat(workout).usingRecursiveComparison().isEqualTo(updatedWorkoutExpected);
    }

    @Test
    void updateWorkoutWhenUnAuth() throws Exception {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void updateWorkoutNotOwnWhenUserTry() throws Exception {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        updatedWorkoutTo.setId(ADMIN_WORKOUT1_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void updateWorkoutWithNotOwnUserIdWhenUserTry() throws Exception {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + ADMIN_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void updateWorkoutNotOwnWhenAdminTry() throws Exception {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        Workout updatedWorkoutExpected = getUpdatedWorkout();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID)
                .headers(adminJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Workout workout = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), Workout.class);
        assertThat(workout).usingRecursiveComparison().isEqualTo(updatedWorkoutExpected);
    }

    @Test
    void updateWorkoutNotFound() throws Exception {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        updatedWorkoutTo.setId(NOT_FOUND_WORKOUT_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + NOT_FOUND_WORKOUT_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_FOUND_RESPONSE);
    }

    @Test
    void updateWorkoutWhenIdsNotTheSame() throws Exception {
        WorkoutTo updatedWorkoutTo = getUpdatedWorkoutTo();
        updatedWorkoutTo.setId(NOT_FOUND_WORKOUT_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedWorkoutTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_MUST_BE_WITH_ID_RESPONSE);
    }

    @Test
    void deleteWorkout() throws Exception {
        perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> workoutService.getWorkoutById(USER1_WORKOUT1_ID, USER1_ID));
    }

    @Test
    void deleteWorkoutWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void deleteWorkoutNotOwnWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_OWN_NOT_FOUND_RESPONSE);
    }

    @Test
    void deleteWorkoutWithNotOwnUserIdWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + ADMIN_ID + WORKOUTS_URI + "/" + ADMIN_WORKOUT1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void deleteWorkoutNotOwnWhenAdminTry() throws Exception {
        perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + USER1_WORKOUT1_ID)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> workoutService.getWorkoutById(USER1_WORKOUT1_ID, USER1_ID));
    }

    @Test
    void deleteWorkoutNotFound() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID + WORKOUTS_URI + "/" + NOT_FOUND_WORKOUT_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(WORKOUT_NOT_FOUND_RESPONSE);
    }

}