package com.breno.garage_service.enumerate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos possíveis de status das vagas")
public enum SpotStatus { AVAILABLE, OCCUPIED }
