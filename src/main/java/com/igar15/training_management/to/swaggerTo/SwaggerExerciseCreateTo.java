package com.igar15.training_management.to.swaggerTo;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema
public class SwaggerExerciseCreateTo {

    @Schema(example = "15")
    private int quantity;

    @Schema(example = "1111")
    private Long workoutId;

    @Schema(example = "3333")
    private Long exerciseTypeId;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public Long getExerciseTypeId() {
        return exerciseTypeId;
    }

    public void setExerciseTypeId(Long exerciseTypeId) {
        this.exerciseTypeId = exerciseTypeId;
    }
}
