package com.breno.garage_service.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "sectors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String code;

    @Column(name="base_price", nullable = false)
    private BigDecimal basePrice = BigDecimal.ZERO;

    @Column(name="max_capacity", nullable=false)
    private Integer maxCapacity;
}