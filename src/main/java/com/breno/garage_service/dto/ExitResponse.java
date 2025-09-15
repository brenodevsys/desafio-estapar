package com.breno.garage_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Resposta de saída processada")
public record ExitResponse(
        @Schema(description = "Valor total cobrado", example = "20.50")
        BigDecimal charged
) {}

