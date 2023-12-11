package com.food.ordring.system.order.service.domain.entity;

import com.food.ordring.system.domain.entity.AggregateRoot;
import com.food.ordring.system.domain.valueobject.RestaurantId;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

    private final List<Product> products;
    private boolean active;

    Restaurant(RestaurantId restaurantId, List<Product> products, boolean active) {
        setId(restaurantId);
        this.products = products;
        this.active = active;
    }

    public static RestaurantBuilder builder() {
        return new RestaurantBuilder();
    }


    public static class RestaurantBuilder {

        private RestaurantId id;
        private List<Product> products;
        private boolean active;

        RestaurantBuilder() {
        }

        public RestaurantBuilder id(UUID uuid) {
            this.id = new RestaurantId(uuid);
            return this;
        }

        public RestaurantBuilder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public RestaurantBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(id, products, active);
        }

        public String toString() {
            return "Restaurant.RestaurantBuilder(products=" + this.products + ", active=" + this.active + ")";
        }
    }
}
