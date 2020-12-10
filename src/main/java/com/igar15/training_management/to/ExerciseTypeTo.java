package com.igar15.training_management.to;

import com.igar15.training_management.utils.MeasureExist;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ExerciseTypeTo extends AbstractBaseTo {

    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 100, message = "Name length must be between 2 and 100 characters")
    private String name;

    @MeasureExist
    @NotBlank(message = "Measure must not be blank")
    private String measure;

    public ExerciseTypeTo() {
    }

    public ExerciseTypeTo(Long id, String name, String measure) {
        super(id);
        this.name = name;
        this.measure = measure;
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
