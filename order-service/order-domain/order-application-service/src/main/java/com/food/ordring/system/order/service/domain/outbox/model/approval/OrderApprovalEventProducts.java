package com.food.ordring.system.order.service.domain.outbox.model.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderApprovalEventProducts {

    @JsonProperty
    private String id;
    @JsonProperty
    private Integer quantity;


}
