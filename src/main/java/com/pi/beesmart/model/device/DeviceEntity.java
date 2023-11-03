package com.pi.beesmart.model.device;

public class DeviceEntity {
    public int id;
    public int gpio;
    public String name;
    public int type;
    public int value;

    public DeviceEntity(int id, int gpio, String name, int type, int value) {
        this.id = id;
        this.gpio = gpio;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public DeviceEntity() {
    }
}
