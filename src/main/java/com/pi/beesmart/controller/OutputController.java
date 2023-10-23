package com.pi.beesmart.controller;

import com.pi.beesmart.model.OutputDAO;
import com.pi.beesmart.model.OutputEntity;
import com.pi4j.io.gpio.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class OutputController {
    private static final GpioPinDigitalOutput[] pin = new GpioPinDigitalOutput[31];


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

        getPin(gpio).setState(dto.status);

        final OutputConverter converter = new OutputConverter();
        return converter.toDTO(new OutputDAO().update(converter.toEntity(dto), gpio));
    }
    public GpioPinDigitalOutput getPin(int pinNum) {
        if (pin[14] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            pin[14] = gpioPi.provisionDigitalOutputPin(RaspiPin.GPIO_14, PinState.LOW);
        }
        if (pin[15] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            pin[15] = gpioPi.provisionDigitalOutputPin(RaspiPin.GPIO_15, PinState.LOW);
        }
        if (pin[23] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            pin[23] = gpioPi.provisionDigitalOutputPin(RaspiPin.GPIO_23, PinState.LOW);
        }
        if (pin[24] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            pin[24] = gpioPi.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW);
        }
        if (pin[25] == null) {
            GpioController gpioPi = GpioFactory.getInstance();
            pin[25] = gpioPi.provisionDigitalOutputPin(RaspiPin.GPIO_25, PinState.LOW);
        }

        return pin[pinNum];
    }
}
