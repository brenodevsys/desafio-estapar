package com.breno.garage_service.service;

import com.breno.garage_service.entity.ParkingSession;
import com.breno.garage_service.enumerate.SpotStatus;
import com.breno.garage_service.repository.SpotRepository;
import com.breno.garage_service.util.PricingConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final SpotRepository spotRepository;

    public BigDecimal factorByOccupancy() {
        long totalSpots = spotRepository.count();
        long occupiedSpots = spotRepository.countByStatus(SpotStatus.OCCUPIED);

        if (totalSpots == 0) return PricingConstants.NORMAL_PRICE;

        double ratio = (double) occupiedSpots / totalSpots;

        if (ratio < 0.25) return PricingConstants.DISCOUNT_LOW;
        if (ratio < 0.50) return PricingConstants.NORMAL_PRICE;
        if (ratio < 0.75) return PricingConstants.SURCHARGE_MEDIUM;
        return PricingConstants.SURCHARGE_HIGH;
    }

    public BigDecimal calculateTotalCharge(ParkingSession session, long totalMinutes) {
        Objects.requireNonNull(session, "Parking session cannot be null");

        BigDecimal billableHours = calculateBillableHours(totalMinutes);
        return session.getEffectivePriceHour().multiply(billableHours);
    }

    private BigDecimal calculateBillableHours(long totalMinutes) {
        long minutesBeyondFree = Math.max(0, totalMinutes - PricingConstants.FREE_MINUTES);
        long billableHours = (long) Math.ceil(minutesBeyondFree / 60.0);
        return BigDecimal.valueOf(billableHours);
    }
}
