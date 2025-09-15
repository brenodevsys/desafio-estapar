package com.breno.garage_service.mapper;

import com.breno.garage_service.dto.ParkingSessionDTO;
import com.breno.garage_service.entity.ParkingSession;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface ParkingSessionMapper {

    ParkingSessionDTO toDto(ParkingSession entity);

}
