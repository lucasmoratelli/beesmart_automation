package com.pi.beesmart.controller;

import com.pi.beesmart.model.OutputDAO;
import com.pi.beesmart.model.OutputEntity;
import com.pi4j.io.gpio.*;
import jdk.jfr.Frequency;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class DeviceController {
    private static final GpioPinDigitalOutput[] out = new GpioPinDigitalOutput[31];
    private static final GpioPinDigitalInput[] in = new GpioPinDigitalInput[31];


    @GetMapping("/output/")
    public List<OutputDTO> getOutput() {
        OutputDAO outputDAO = new OutputDAO();
        List<OutputEntity> outputs = outputDAO.getAll();

        return new OutputConverter().toDTO(outputs);
    }
    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @PutMapping("/output/{gpio}")
    public OutputDTO putOutput(@RequestBody OutputDTO dto, @PathVariable int gpio) {

        getOutPin(gpio).setState(dto.status);
        System.out.println(getOutPin(gpio).getState());

        final OutputConverter converter = new OutputConverter();
        return converter.toDTO(new OutputDAO().update(converter.toEntity(dto), gpio));
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
        if (in[7].isHigh()) {
            deviceController.getOutPin(15).toggle();
        }
    }
}
