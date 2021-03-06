package com.igar15.training_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.igar15.training_management.entity.abstracts.AbstractBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "exercises")
public class Exercise extends AbstractBaseEntity {

    @Column(name = "quantity")
    @Min(1)
    @Schema(example = "15")
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_type_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ExerciseType exerciseType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Workout workout;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Exercise() {
    }

    public Exercise(Long id, int quantity) {
        super(id);
        this.quantity = quantity;
    }

    public Exercise(Long id, int quantity, ExerciseType exerciseType, Workout workout, User user) {
        super(id);
        this.quantity = quantity;
        this.exerciseType = exerciseType;
        this.workout = workout;
        this.user = user;
    }

    public Exercise(Long id, int quantity, ExerciseType exerciseType) {
        super(id);
        this.quantity = quantity;
        this.exerciseType = exerciseType;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
