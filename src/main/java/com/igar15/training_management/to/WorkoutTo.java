package com.igar15.training_management.to;

import com.igar15.training_management.to.abstracts.AbstractBaseTo;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class WorkoutTo extends AbstractBaseTo {

    @NotNull(message = "DateTime must not be null")
    @Schema(example = "2020-12-16T15:30:00")
    private LocalDateTime dateTime;

    public WorkoutTo() {
    }

    public WorkoutTo(Long id, LocalDateTime dateTime) {
        super(id);
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "WorkoutTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                '}';
    }
}
