package com.breno.garage_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa uma sessão de estacionamento")
public class ParkingSessionDTO {

    @Schema(description = "Identificador da sessão", example = "101")
    private Long id;

    @Schema(description = "Placa do veículo", example = "ABC1234")
    private String plate;

    @Schema(description = "Identificador do setor", example = "1")
    private Long sectorId;

    @Schema(description = "Identificador da vaga", example = "10")
    private Long spotId;

    @Schema(type = "string", format = "date-time", description = "Data/hora de entrada", example = "2025-01-01T12:00:00")
    private LocalDateTime entryTime;

    @Schema(type = "string", format = "date-time", description = "Data/hora em que estacionou", example = "2025-01-01T12:05:00")
    private LocalDateTime parkedTime;

    @Schema(type = "string", format = "date-time", description = "Data/hora de saída", example = "2025-01-01T14:30:00")
    private LocalDateTime exitTime;

    @Schema(description = "Fator dinâmico de preço aplicado", example = "1.10")
    private BigDecimal priceFactor;

    @Schema(description = "Preço efetivo por hora", example = "11.00")
    private BigDecimal effectivePriceHour;

    @Schema(description = "Valor total cobrado", example = "22.00")
    private BigDecimal chargedAmount;

    @Schema(description = "Status da sessão", example = "OPEN")
    private String status;
}
