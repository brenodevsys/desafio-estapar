package com.breno.garage_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Configuração de uma vaga no estacionamento")
public class SpotConfigDTO {

    @JsonProperty("id")
    @Schema(description = "Identificador único da vaga", example = "10")
    private Long id;

    @JsonProperty("sector")
    @Schema(description = "Código do setor da vaga", example = "A")
    private String sector;

    @JsonProperty("lat")
    @Schema(description = "Latitude da vaga", example = "-23.561684")
    private double lat;

    @JsonProperty("lng")
    @Schema(description = "Longitude da vaga", example = "-46.655981")
    private double lng;
}
