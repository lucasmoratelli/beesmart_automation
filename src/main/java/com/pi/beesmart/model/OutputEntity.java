package com.pi.beesmart.model;

public class OutputEntity {
    public int gpio;
    public String name;
    public boolean status;
    public String type;
    public int temperature;

    public OutputEntity(int gpio, String name, boolean status, String type, int  temperature ) {
        this.gpio = gpio;
        this.name = name;
        this.status = status;
        this.type = type;
        this.temperature = temperature;
    }

    public OutputEntity() {
    }
}
