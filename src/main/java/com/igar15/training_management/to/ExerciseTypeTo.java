package com.igar15.training_management.to;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ExerciseTypeTo {

    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 100, message = "Name length must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Measure must not be blank")
    private String measure;

    public ExerciseTypeTo() {
    }

    public ExerciseTypeTo(Long id, String name, String measure) {
        this.id = id;
        this.name = name;
        this.measure = measure;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public String toString() {
        return "ExerciseTypeTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", measure='" + measure + '\'' +
                '}';
    }
}
