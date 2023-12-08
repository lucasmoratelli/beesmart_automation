package com.pi.beesmart.controller.routine;

import com.pi.beesmart.controller.device.DeviceController;
import com.pi.beesmart.controller.device.DeviceConverter;
import com.pi.beesmart.model.device.DeviceDAO;
import com.pi.beesmart.model.routine.RoutineDAO;
import com.pi.beesmart.model.routine.RoutineEntity;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.net.http.WebSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Component
@EnableScheduling
@CrossOrigin(origins = "*")
@RestController
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

    private int i = 0;
    private int j = 0;

    @CrossOrigin(origins = "*")
    @GetMapping("/Routines/")
    public List<RoutineDTO> getRoutines() {
        List<RoutineEntity> routines = routineDAO.getAllRoutines();
        return routineConverter.toDTO(routines);
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/Routines/")
    public RoutineDTO postRoutine(@RequestBody RoutineDTO dto) {
        RoutineEntity routinePostada = routineDAO.add(routineConverter.toEntity(dto));

        return routineConverter.toDTO(routinePostada);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/Routines/{id}")
    public ResponseEntity<RoutineDTO> deletePessoa(@PathVariable int id) {
        RoutineEntity entity = routineDAO.delete(id);

        if (entity == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(routineConverter.toDTO(entity));
    }

    //Método responsável por carregar as rotinas cadastradas no banco de dados para que possam ser executadas
    @Scheduled(fixedDelay = 1000)
    public void executeRoutineTime() {
        List<RoutineEntity> routines = routineDAO.getAllRoutines();
        Date dataHoraAtual = new Date();
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);

        for (RoutineEntity routineEntity : routines) {
            if (routineEntity.type == 0) {
                if (hora.equals(routineEntity.time)) {
                    if (routineEntity.action == 0) {

                        System.out.println();
                        System.out.println(routineEntity.actuatorId);
                        deviceController.setState(routineEntity.actuatorId, 0, false);

                    }
                    if (routineEntity.action == 1) {

                        deviceController.setState(routineEntity.actuatorId, 0, true);

                    }
                }
            }
        }
    }

    //Método responsável por executar as rotinas
    @Scheduled(fixedRate = 5000)
    public void executeRoutineONOFF() {

        List<RoutineEntity> routines = routineDAO.getAllRoutines();

        GpioPinDigitalInput interruptorSala = deviceController.getInPin(8);
        GpioPinDigitalInput interruptorCozinha = deviceController.getInPin(9);
        GpioPinDigitalInput interruptorSuite = deviceController.getInPin(0);
        GpioPinDigitalInput interruptorHidro = deviceController.getInPin(2);

        GpioPinListenerDigital listenerInterruptorSala = (GpioPinDigitalStateChangeEvent event) -> {
            System.out.println(event.getState());
            for (RoutineEntity routine : routines) {
                if (routine.sensorId == 5) {
                    if (interruptorSala.isHigh()) {
                        deviceController.toggleState(routine.sensorId, 1);
                        deviceController.toggleState(routine.actuatorId, 0);
                    }
                }
            }
        };

        GpioPinListenerDigital listenerInterruptorCozinha = (GpioPinDigitalStateChangeEvent event) -> {
            System.out.println(event.getState());
            for (RoutineEntity routine : routines) {
                if (routine.sensorId == 6) {
                    if (interruptorCozinha.isHigh()){
                        deviceController.toggleState(routine.sensorId, 1);
                        deviceController.toggleState(routine.actuatorId, 0);
                    }
                }
            }
        };

        GpioPinListenerDigital listenerInterruptorSuite = (GpioPinDigitalStateChangeEvent event) -> {
            System.out.println(event.getState());
            for (RoutineEntity routine : routines) {
                if (routine.sensorId == 7) {
                    if (interruptorSuite.isHigh()){
                    deviceController.toggleState(routine.sensorId, 1);
                    deviceController.toggleState(routine.actuatorId, 0);
                    }
                }
            }
        };

        GpioPinListenerDigital listenerInterruptorHidro = (GpioPinDigitalStateChangeEvent event) -> {
            System.out.println(event.getState());
            for (RoutineEntity routine : routines) {
                if (routine.sensorId == 8) {
                    if (interruptorHidro.isHigh()){
                    deviceController.toggleState(routine.sensorId, 1);
                    deviceController.toggleState(routine.actuatorId, 0);
                    }
                }
            }
        };

        if (i == 0){
            interruptorSala.addListener(listenerInterruptorSala);
            interruptorCozinha.addListener(listenerInterruptorCozinha);
            interruptorSuite.addListener(listenerInterruptorSuite);
            interruptorHidro.addListener(listenerInterruptorHidro);
            i = 1;
        }
    }
}
