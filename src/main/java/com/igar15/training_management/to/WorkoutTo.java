package com.igar15.training_management.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.igar15.training_management.to.abstracts.AbstractBaseTo;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class WorkoutTo extends AbstractBaseTo {

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm") // we can set which pattern use to serialize/deserialize dateTime
    @NotNull(message = "DateTime must not be null")
    @Schema(example = "2020-12-16T15:30:00")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
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
