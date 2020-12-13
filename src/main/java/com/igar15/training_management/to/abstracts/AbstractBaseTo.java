package com.igar15.training_management.to.abstracts;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public abstract class AbstractBaseTo {

    @Schema(example = "1111")
    protected Long id;

    public AbstractBaseTo() {
    }

    public AbstractBaseTo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
