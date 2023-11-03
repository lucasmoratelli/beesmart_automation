package com.pi.beesmart.controller.device;

import com.pi.beesmart.model.device.DeviceDAO;
import com.pi.beesmart.model.device.DeviceEntity;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class DeviceController {
    private static final GpioPinDigitalOutput[] out = new GpioPinDigitalOutput[31];
    private static final GpioPinDigitalInput[] in = new GpioPinDigitalInput[31];


    @GetMapping("/actuator/")
    public List<DeviceDTO> getOutput() {
        DeviceDAO deviceDAO = new DeviceDAO();
        List<DeviceEntity> devices = deviceDAO.getAllActuators();
        for (DeviceEntity device : devices) {
            if (getOutPin(device.gpio).isHigh()) {
                device.value = 1;
            } else {
                device.value = 0;
            }
        }
        return new DeviceConverter().toDTO(devices);
    }
    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/toggle/{id}")
    public boolean toggleAndLog(@PathVariable int id) {
        DeviceDAO deviceDAO = new DeviceDAO();
        DeviceConverter deviceConverter = new DeviceConverter();
        DeviceDTO device = deviceConverter.toDTO(deviceDAO.getActuatorById(id));
        getOutPin(device.gpio).toggle();
        var pinState = getOutPin(device.gpio).getState();
        int value;
        if (pinState.isHigh()){
            value = 1;
        } else {
            value = 0;
        }
        deviceDAO.addLog(id, device.type, value);
        return pinState.isHigh();
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
    public void getInPin() {
        DeviceController deviceController = new DeviceController();
        if (in[7] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            in[7] = gpioPi.provisionDigitalInputPin(RaspiPin.GPIO_07);
        }
        if (in[0] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            in[0] = gpioPi.provisionDigitalInputPin(RaspiPin.GPIO_00);
        }
        if (in[2] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            in[2] = gpioPi.provisionDigitalInputPin(RaspiPin.GPIO_02);
        }
        in[7].addListener((GpioPinListenerDigital) event -> {
            // display pin state on console
            if (in[7].isHigh()) {
                getOutPin(15).toggle();
                System.out.println("pressed");
            }
         });
    }
}
