package com.igar15.training_management.to;

import java.time.LocalDateTime;

public class WorkoutTo {

    private Long id;
    private LocalDateTime dateTime;

    public WorkoutTo() {
    }

    public WorkoutTo(Long id, LocalDateTime dateTime) {
        this.id = id;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
