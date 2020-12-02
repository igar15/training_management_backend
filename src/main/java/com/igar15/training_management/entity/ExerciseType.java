package com.igar15.training_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.igar15.training_management.entity.abstracts.AbstractNamedEntity;
import com.igar15.training_management.entity.enums.Measure;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "exercise_types")
public class ExerciseType extends AbstractNamedEntity {

    @Column(name = "measure")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Measure measure;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    public ExerciseType() {
    }

    public ExerciseType(Long id, String name, Measure measure) {
        super(id, name);
        this.measure = measure;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ExerciseType{" +
                "id=" + id +
                ", name=" + name +
                ", measure=" + measure +
                '}';
    }
}
