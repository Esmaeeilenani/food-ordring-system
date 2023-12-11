package com.food.ordring.system.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract public class AggregateRoot<ID> extends BaseEntity<ID> {
}
