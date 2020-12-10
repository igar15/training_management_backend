package com.igar15.training_management.to.abstracts;

public abstract class AbstractBaseTo {

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
