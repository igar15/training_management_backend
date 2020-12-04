package com.igar15.training_management.to;

public class ExerciseTo {

    private long id;
    private int quantity;
    private long workoutId;
    private long exerciseTypeId;

    public ExerciseTo() {
    }

    public ExerciseTo(long id, int quantity, long workoutId, long exerciseTypeId) {
        this.id = id;
        this.quantity = quantity;
        this.workoutId = workoutId;
        this.exerciseTypeId = exerciseTypeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(long workoutId) {
        this.workoutId = workoutId;
    }

    public long getExerciseTypeId() {
        return exerciseTypeId;
    }

    public void setExerciseTypeId(long exerciseTypeId) {
        this.exerciseTypeId = exerciseTypeId;
    }
}
