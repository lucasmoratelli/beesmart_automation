package com.pi.beesmart.controller.routine;

public class RoutineDTO {
    public int id;
    public int type;
    public String name;
    public String time;
    public int comparation;
    public int action;
    public Integer sensorId;
    public Integer actuatorId;

    public RoutineDTO(int id, int type, String name, String time, int comparation, int action, Integer sensorId, Integer actuatorId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.time = time;
        this.comparation = comparation;
        this.action = action;
        this.sensorId = sensorId;
        this.actuatorId = actuatorId;
    }
}
