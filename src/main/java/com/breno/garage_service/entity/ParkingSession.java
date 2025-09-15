package com.breno.garage_service.entity;

import com.breno.garage_service.enumerate.SessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16)
    private String plate;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "parked_time")
    private LocalDateTime parkedTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "price_factor", nullable = false)
    private BigDecimal priceFactor = BigDecimal.ONE;

    @Column(name = "effective_price_hour")
    private BigDecimal effectivePriceHour = BigDecimal.ZERO;

    @Column(name = "charged_amount")
    private BigDecimal chargedAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.ENTRY;
}
