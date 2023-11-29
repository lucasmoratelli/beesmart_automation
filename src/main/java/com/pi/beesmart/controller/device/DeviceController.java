package com.pi.beesmart.controller.device;

import com.pi.beesmart.model.device.DeviceDAO;
import com.pi.beesmart.model.device.DeviceEntity;
import com.pi4j.io.gpio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Component
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class DeviceController {
    private static final GpioPinDigitalOutput[] out = new GpioPinDigitalOutput[31];
    private static final GpioPinDigitalInput[] in = new GpioPinDigitalInput[31];
    @Autowired
    public DeviceDAO deviceDAO;
    @Autowired
    public DeviceConverter deviceConverter;

    @GetMapping("/actuator/")
    public List<DeviceDTO> getOutput() {
        List<DeviceEntity> devices = deviceDAO.getAllActuators();
        for (DeviceEntity device : devices) {
            if (getOutPin(device.gpio).isHigh()) {
                device.value = 1;
            } else {
                device.value = 0;
            }
        }
        return deviceConverter.toDTO(devices);
    }
    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/toggle/{id}")
    public boolean getToggle(@PathVariable int id) {
        int gpio = toggleState(id, 0).gpio;

        return getOutPin(gpio).isHigh();
    }

    public DeviceDTO toggleState(int id, int type) {
        DeviceDTO device = deviceConverter.toDTO(deviceDAO.getDeviceById(id, type));
        System.out.println(device.gpio);
        System.out.println(device.name);
        if (type == 0) {
            getOutPin(device.gpio).toggle();
            var pinState = getOutPin(device.gpio).getState();
            int value;
            if (pinState.isHigh()){
                value = 1;
            } else {
                value = 0;
            }
            deviceDAO.addLog(id, device.type, value);
        }else {
            //Future Implementation
            deviceDAO.addLog(id, type, 1);
        }
        return device;
    }
    public DeviceDTO setState(int id, int type, boolean state) {
        DeviceDTO device = deviceConverter.toDTO(deviceDAO.getDeviceById(id, type));
        System.out.println(device.gpio);
        System.out.println(device.name);
        if (type == 0) {
            getOutPin(device.gpio).setState(state);
            int value;
            if (state){
                value = 1;
            } else {
                value = 0;
            }
            deviceDAO.addLog(id, device.type, value);
        }else {
            //Future Implementation
            deviceDAO.addLog(id, type, 1);
        }
        return device;
    }
    public GpioPinDigitalOutput getOutPin(int pinNum) {
        if (out[15] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            out[15] = gpioPi.provisionDigitalOutputPin(RaspiPin.GPIO_15, PinState.LOW);
        }
        if (out[16] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            out[16] = gpioPi.provisionDigitalOutputPin(RaspiPin.GPIO_16, PinState.LOW);
        }
        if (out[4] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            out[4] = gpioPi.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
        }
        if (out[5] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            out[5] = gpioPi.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
        }

        return out[pinNum];
    }
    public GpioPinDigitalInput getInPin(int pinNum) {
        if (in[8] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            in[8] = gpioPi.provisionDigitalInputPin(RaspiPin.GPIO_08);
        }
        if (in[9] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            in[9] = gpioPi.provisionDigitalInputPin(RaspiPin.GPIO_09);
        }
        if (in[0] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            in[0] = gpioPi.provisionDigitalInputPin(RaspiPin.GPIO_00);
        }
        if (in[2] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            in[2] = gpioPi.provisionDigitalInputPin(RaspiPin.GPIO_02);
        }
        return in[pinNum];
    }
}
