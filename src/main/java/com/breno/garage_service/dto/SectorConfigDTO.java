package com.breno.garage_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Schema(description = "Configuração de um setor da garagem")
public class SectorConfigDTO {

    @JsonProperty("sector")
    @Schema(description = "Código do setor", example = "A")
    private String sector;

    @JsonProperty("base_price")
    @Schema(description = "Preço base por hora no setor", example = "10.00")
    private BigDecimal basePrice;

    @JsonProperty("max_capacity")
    @Schema(description = "Capacidade máxima de vagas no setor", example = "100")
    private int maxCapacity;
}
