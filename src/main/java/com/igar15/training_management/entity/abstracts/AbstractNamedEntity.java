package com.igar15.training_management.entity.abstracts;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
public class AbstractNamedEntity extends AbstractBaseEntity {

    @NotBlank
    @Size(min = 2)
    @Column(name = "name")
    protected String name;

    public AbstractNamedEntity() {
    }

    public AbstractNamedEntity(Long id, @NotBlank @Size(min = 2) String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
