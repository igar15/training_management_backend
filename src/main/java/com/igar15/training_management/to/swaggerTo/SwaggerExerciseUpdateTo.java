package com.igar15.training_management.to.swaggerTo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class SwaggerExerciseUpdateTo {

    @Schema(example = "3333")
    private Long id;

    @Schema(example = "15")
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
