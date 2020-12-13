package com.igar15.training_management.to.swaggerTo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class SwaggerExerciseTypeCreateTo {

    @Schema(example = "Pushups")
    private String name;

    @Schema(example = "Times")
    private String measure;

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
}
