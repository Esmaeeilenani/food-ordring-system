package com.food.ordring.system.order.service.dataaccess.customer.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order_customer_m_view",schema = "customer" )
@Entity
public class CustomerEntity {
    @Id
    private UUID id;


}
