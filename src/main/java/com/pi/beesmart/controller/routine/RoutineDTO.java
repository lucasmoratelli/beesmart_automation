package com.pi.beesmart.controller.routine;

public class RoutineDTO {
    public int id;
    public String name;
    public int value;
    public int comparation;
    public int action;
    public Integer sensorId;
    public Integer actuatorId;

    public RoutineDTO(int id, String name, int value, int comparation, int action, Integer sensorId, Integer actuatorId) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.comparation = comparation;
        this.action = action;
        this.sensorId = sensorId;
        this.actuatorId = actuatorId;
    }
}
