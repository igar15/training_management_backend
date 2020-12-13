package com.igar15.training_management.to.swaggerTo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema
public class SwaggerWorkoutCreateTo {

    @Schema(example = "2020-12-16T15:30:00")
    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
