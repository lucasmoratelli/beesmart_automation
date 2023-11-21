package com.pi.beesmart.controller.routine;

import com.pi.beesmart.controller.device.DeviceController;
import com.pi.beesmart.controller.device.DeviceConverter;
import com.pi.beesmart.model.device.DeviceDAO;
import com.pi.beesmart.model.routine.RoutineDAO;
import com.pi.beesmart.model.routine.RoutineEntity;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Component
@EnableScheduling
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class RoutineController {
    @Autowired
    public RoutineConverter routineConverter;

    @Autowired
    public RoutineDAO routineDAO;

    @Autowired
    public DeviceDAO deviceDAO;

    @Autowired
    public DeviceController deviceController;

    @GetMapping("/Routines/")
    public List<RoutineDTO> getRoutines() {
        RoutineDAO routineDAO = new RoutineDAO();
        List<RoutineEntity> routines = routineDAO.getAllRoutines();
        return routineConverter.toDTO(routines);
    }

    @Scheduled(fixedDelay = 100)
    public void executeRoutine() {

        while (true) {
            List<RoutineEntity> routines = routineDAO.getAllRoutines();
            Date dataHoraAtual = new Date();
            String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
            for (RoutineEntity routine : routines) {

                if (routine.type == 1) {
                    GpioPinDigitalInput sensorGpio = deviceController.getInPin(deviceDAO.getDeviceById(routine.sensorId, 1).gpio);
                    sensorGpio.addListener((GpioPinListenerDigital) event -> {

                        if (sensorGpio.isHigh()) {

                            deviceController.toggleState(routine.sensorId, 1);
                            deviceController.toggleState(routine.actuatorId, 0);

                        }

                    });

                } else if (routine.type == 0) {
                    if (hora.equals(routine.time)) {
                        if (routine.action == 0) {

                            deviceController.setState(routine.actuatorId, 0, false);

                        }
                        if (routine.action == 1) {

                            deviceController.setState(routine.actuatorId, 0, true);

                        }
                    }
                }
            }
        }
    }
}
