package com.tech.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemRequest {

    private String productId;

    @NotBlank
    private String offerId;

    @NotNull
    @Min(1)
    private Integer quantity;

    public AddItemRequest() {}

}
