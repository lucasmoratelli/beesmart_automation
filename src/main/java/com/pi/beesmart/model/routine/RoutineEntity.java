package com.pi.beesmart.model.routine;

public class RoutineEntity {
    public int id;
    public String name;
    public int value;
    public int comparation;
    public int action;
    public int sensorId;
    public int actuatorId;

    public RoutineEntity(int id, String name, int value, int comparation, int action, int sensorId, int actuatorId) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.comparation = comparation;
        this.action = action;
        this.sensorId = sensorId;
        this.actuatorId = actuatorId;
    }
}
