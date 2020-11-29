package com.igar15.training_management.entity;

import com.igar15.training_management.entity.abstracts.AbstractBaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "exercises")
public class Exercise extends AbstractBaseEntity {

    @Column(name = "quantity")
    @NotNull
    @Min(1)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_type_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ExerciseType exerciseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Workout workout;

    public Exercise() {
    }

    public Exercise(long id, int quantity, ExerciseType exerciseType, Workout workout) {
        super(id);
        this.quantity = quantity;
        this.exerciseType = exerciseType;
        this.workout = workout;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
