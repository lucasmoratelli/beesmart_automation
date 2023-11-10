package com.pi.beesmart.controller.routine;

import com.pi.beesmart.controller.device.DeviceDTO;
import com.pi.beesmart.model.device.DeviceEntity;
import com.pi.beesmart.model.routine.RoutineEntity;

import java.util.List;
import java.util.stream.Collectors;

public class RoutineConverter {
    public List<RoutineDTO> toDTO(List<RoutineEntity> entities) {

        return entities //
                .stream() //
                .map(entity -> new RoutineDTO(entity.id, entity.name, entity.value, entity.comparation, entity.action, entity.sensorId, entity.actuatorId)) //
                .collect(Collectors.toList());
    }

    public RoutineDTO toDTO(RoutineEntity entity) {
        return new RoutineDTO(entity.id, entity.name, entity.value, entity.comparation, entity.action, entity.sensorId, entity.actuatorId);
    }

    public RoutineEntity toEntity(RoutineDTO dto) {
        return new RoutineEntity(dto.id, dto.name, dto.value, dto.comparation, dto.action, dto.sensorId, dto.actuatorId);
    }
}
