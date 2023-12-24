package com.food.ordring.system.order.service.dataaccess.restaurant.entity;


import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order_restaurant_m_view", schema = "restaurant")
@IdClass(RestaurantEntityId.class)
@Entity
public class RestaurantEntity {
    @Id
    @Column(name = "restaurant_id")
    private UUID id;
    @Id
    private UUID productId;
    private String restaurantName;

    @Column(name = "restaurant_active")
    private Boolean active;
    private String productName;
    private BigDecimal productPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return id.equals(that.id) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId);
    }
}
