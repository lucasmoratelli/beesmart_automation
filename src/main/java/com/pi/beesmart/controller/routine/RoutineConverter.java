package com.pi.beesmart.controller.routine;

import com.pi.beesmart.model.routine.RoutineEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class RoutineConverter {
    public List<RoutineDTO> toDTO(List<RoutineEntity> entities) {

        return entities //
                .stream() //
                .map(entity -> new RoutineDTO(entity.id, entity.type, entity.name, entity.time, entity.comparation, entity.action, entity.sensorId, entity.actuatorId)) //
                .collect(Collectors.toList());
    }

    public RoutineDTO toDTO(RoutineEntity entity) {
        return new RoutineDTO(entity.id, entity.type, entity.name, entity.time, entity.comparation, entity.action, entity.sensorId, entity.actuatorId);
    }

    public RoutineEntity toEntity(RoutineDTO dto) {
        return new RoutineEntity(dto.id, dto.type, dto.name, dto.time, dto.comparation, dto.action, dto.sensorId, dto.actuatorId);
    }
}
