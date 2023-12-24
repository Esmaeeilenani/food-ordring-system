package com.food.ordring.system.order.service.dataaccess.customer.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
