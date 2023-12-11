package com.food.ordring.system.order.service.domain.entity;

import com.food.ordring.system.domain.entity.BaseEntity;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.ProductId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId ,String name, Money price) {
        this.setId(productId);
        this.name = name;
        this.price = price;
    }

    public void updateFiled(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
    }
}
