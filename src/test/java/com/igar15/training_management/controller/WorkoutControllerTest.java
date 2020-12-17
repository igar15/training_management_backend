package com.igar15.training_management.controller;

import com.igar15.training_management.AbstractControllerTest;
import com.igar15.training_management.ControllerTestData;
import com.igar15.training_management.UserTestData;
import com.igar15.training_management.WorkoutTestData;
import com.igar15.training_management.entity.Workout;
import com.igar15.training_management.utils.JsonUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.igar15.training_management.ControllerTestData.*;
import static com.igar15.training_management.UserTestData.*;
import static com.igar15.training_management.WorkoutTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class WorkoutControllerTest extends AbstractControllerTest {

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
    void getWorkouts() {
    }

    @Test
    void createWorkout() {
    }

    @Test
    void updateWorkout() {
    }

    @Test
    void deleteWorkout() {
    }
}