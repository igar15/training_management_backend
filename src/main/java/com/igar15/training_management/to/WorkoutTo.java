package com.igar15.training_management.to;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class WorkoutTo extends AbstractBaseTo {

    @NotNull(message = "DateTime must not be null")
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
