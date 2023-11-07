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
    public boolean getToggle(@PathVariable int id) {
        int gpio = toggleAndLog(id, 0).gpio;
        getOutPin(gpio).toggle();

        return getOutPin(gpio).isHigh();
    }

    public DeviceDTO toggleAndLog(int id, int type) {
        DeviceDAO deviceDAO = new DeviceDAO();
        DeviceConverter deviceConverter = new DeviceConverter();
        DeviceDTO device = deviceConverter.toDTO(deviceDAO.getActuatorById(id, type));
        System.out.println(device.gpio);
        System.out.println(device.name);
        if (type == 0) {
            var pinState = getOutPin(device.gpio).getState();
            int value;
            if (pinState.isHigh()){
                value = 1;
            } else {
                value = 0;
            }
            deviceDAO.addLog(id, device.type, value);
        }else {
            deviceDAO.addLog(id, device.type, 1);
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
    public void getInPin() {
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
        in[8].addListener((GpioPinListenerDigital) event -> {

            if (in[8].isHigh()) {

                toggleAndLog(5, 1);
                toggleAndLog(1, 0);
                System.out.println("pressed");
            }
        });
        in[9].addListener((GpioPinListenerDigital) event -> {

            // display pin state on console
            if (in[9].isHigh()) {
                toggleAndLog(6, 1);
                toggleAndLog(2, 0);
                System.out.println("pressed");
            }
        });
        in[0].addListener((GpioPinListenerDigital) event -> {

            // display pin state on console
            if (in[0].isHigh()) {
                toggleAndLog(7, 1);
                toggleAndLog(3, 0);
                System.out.println("pressed");
            }
        });
        in[2].addListener((GpioPinListenerDigital) event -> {

            // display pin state on console
            if (in[2].isHigh()) {
                toggleAndLog(8, 1);
                toggleAndLog(4, 0);
                System.out.println("pressed");
            }
        });
    }
}
