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
    private int i = 0;

    @Scheduled(fixedDelay = 1000)
    public void executeRoutine() {

        List<RoutineEntity> routines = routineDAO.getAllRoutines();
        Date dataHoraAtual = new Date();
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
        System.out.println("ROTINAS CADASTRADAS");
        System.out.println("ID Rotina\tNome\t\tTipo\tID Sensor\tID Atuador\tAcao\tComparacao\tHoraAC\tHoraAT");

        for (RoutineEntity routine2 : routines) {
            System.out.println(routine2.id + "\t\t" + routine2.name + "\t" + routine2.type + "\t" + routine2.sensorId + "\t\t\t" + routine2.actuatorId + "\t\t\t" + routine2.action + "\t" + routine2.comparation + "\t" + routine2.time + "\t" + hora);
        }
        if (i == 0) {
            for (RoutineEntity routine : routines) {

                if (routine.type == 1) {
                    GpioPinDigitalInput sensorGpio = deviceController.getInPin(deviceDAO.getDeviceById(routine.sensorId, 1).gpio);
                    sensorGpio.addListener((GpioPinListenerDigital) event -> {

                        if (sensorGpio.isHigh()) {

                            deviceController.toggleState(routine.sensorId, 1);
                            deviceController.toggleState(routine.actuatorId, 0);

                        }

                    });
                }
            }
            i = 1;
        }

        for (RoutineEntity routineEntity : routines) {

            if (routineEntity.type == 1) {
                GpioPinDigitalInput sensorGpio = deviceController.getInPin(deviceDAO.getDeviceById(routineEntity.sensorId, 1).gpio);
                sensorGpio.addListener((GpioPinListenerDigital) event -> {

                    if (sensorGpio.isHigh()) {

                        deviceController.toggleState(routineEntity.sensorId, 1);
                        deviceController.toggleState(routineEntity.actuatorId, 0);

                    }

                });

            } else if (routineEntity.type == 0) {
                if (hora.equals(routineEntity.time)) {
                    if (routineEntity.action == 0) {

                        deviceController.setState(routineEntity.actuatorId, 0, false);

                    }
                    if (routineEntity.action == 1) {

                        deviceController.setState(routineEntity.actuatorId, 0, true);

                    }
                }
            }
        }

    }
}
