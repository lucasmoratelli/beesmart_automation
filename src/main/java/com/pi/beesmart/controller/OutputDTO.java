package com.pi.beesmart.controller;

public class OutputDTO {
    public int gpio;
    public String name;
    public boolean status;
    public String type;
    public int temperature;

    public OutputDTO(int gpio, String name, boolean status, String type, int  temperature ) {
        this.gpio = gpio;
        this.name = name;
        this.status = status;
        this.type = type;
        this.temperature = temperature;
    }

    public OutputDTO() {
    }
}
