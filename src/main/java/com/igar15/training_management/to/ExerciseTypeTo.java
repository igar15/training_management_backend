package com.igar15.training_management.to;

public class ExerciseTypeTo {

    private Long id;
    private String name;
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
