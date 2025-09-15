package com.breno.garage_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa um setor do estacionamento")
public class SectorDTO {

    @Schema(description = "Identificador único do setor", example = "1")
    private Long id;

    @Schema(description = "Código do setor", example = "A")
    private String code;

    @Schema(description = "Preço base por hora neste setor", example = "10.00")
    private BigDecimal basePrice;

    @Schema(description = "Capacidade máxima de vagas no setor", example = "100")
    private int maxCapacity;
}
