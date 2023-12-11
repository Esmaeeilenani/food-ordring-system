package com.food.ordring.system.order.service.domain.entity;

import com.food.ordring.system.domain.entity.BaseEntity;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.ProductId;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(UUID productId, String name, Money price) {
        this.setId(new ProductId(productId));
        this.name = name;
        this.price = price;
    }

    public Product(UUID productId) {
        this.setId(new ProductId(productId));

    }

    public void updateFiled(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
    }
}
