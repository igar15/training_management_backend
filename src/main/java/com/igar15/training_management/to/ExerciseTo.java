package com.igar15.training_management.to;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ExerciseTo {

    private Long id;

    @Min(value = 1, message = "Quantity must be more than zero")
    private int quantity;

    @NotNull(message = "Workout ID must not be null")
    private Long workoutId;

    @NotNull(message = "Exercise type ID must not be null")
    private Long exerciseTypeId;

    public ExerciseTo() {
    }

    public ExerciseTo(Long id, int quantity, Long workoutId, Long exerciseTypeId) {
        this.id = id;
        this.quantity = quantity;
        this.workoutId = workoutId;
        this.exerciseTypeId = exerciseTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "ExerciseTo{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", workoutId=" + workoutId +
                ", exerciseTypeId=" + exerciseTypeId +
                '}';
    }
}
