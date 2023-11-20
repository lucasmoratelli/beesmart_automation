package com.pi.beesmart.model.routine;

public class RoutineEntity {
    public int id;
    public int type;
    public String name;
    public String time;
    public int comparation;
    public int action;
    public int sensorId;
    public int actuatorId;

    public RoutineEntity(int id, int type, String name, String time, int comparation, int action, int sensorId, int actuatorId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.time = time;
        this.comparation = comparation;
        this.action = action;
        this.sensorId = sensorId;
        this.actuatorId = actuatorId;
    }

    public RoutineEntity() {

    }
}
