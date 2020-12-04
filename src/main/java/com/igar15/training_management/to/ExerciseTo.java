package com.igar15.training_management.to;

public class ExerciseTo {

    private Long id;
    private int quantity;
    private Long workoutId;
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
}
