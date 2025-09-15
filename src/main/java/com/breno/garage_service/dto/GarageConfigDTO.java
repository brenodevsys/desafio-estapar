package com.breno.garage_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Configuração completa da garagem retornada pelo simulador")
public class GarageConfigDTO {

    @JsonProperty("garage")
    @Schema(description = "Lista de setores disponíveis")
    private List<SectorConfigDTO> garage;

    @JsonProperty("spots")
    @Schema(description = "Lista de vagas disponíveis")
    private List<SpotConfigDTO> spots;
}
