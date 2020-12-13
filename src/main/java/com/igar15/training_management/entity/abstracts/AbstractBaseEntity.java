package com.igar15.training_management.entity.abstracts;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.Hibernate;

import javax.persistence.*;

@MappedSuperclass
@Schema
public class AbstractBaseEntity {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @Schema(example = "9999")
    protected Long id;

    public AbstractBaseEntity() {
    }

    public AbstractBaseEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(Hibernate.getClass(o))) {
            return false;
        }
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.intValue();
    }

}
