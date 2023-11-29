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
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin(origins = "*")
    @GetMapping("/Routines/")
    public List<RoutineDTO> getRoutines() {
        List<RoutineEntity> routines = routineDAO.getAllRoutines();
        return routineConverter.toDTO(routines);
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/Routines/")
    public RoutineDTO postRoutine(@RequestBody RoutineDTO dto) {

        final RoutineConverter converter = routineConverter;
        return converter.toDTO(routineDAO.add(converter.toEntity(dto)));
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
    private int i = 0;

    @Scheduled(fixedRate = 1000)
    public void executeRoutine() {

        List<RoutineEntity> routines = routineDAO.getAllRoutines();
        Date dataHoraAtual = new Date();
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
        System.out.println("ROTINAS CADASTRADAS");
        System.out.println("ID Rotina\tNome\t\tTipo\tID Sensor\tID Atuador\tAcao\tComparacao\tHoraAC\tHoraAT");

        for (RoutineEntity routine2 : routines) {
            System.out.println(routine2.id + "\t\t" + routine2.name + "\t" + routine2.type + "\t" + routine2.sensorId + "\t\t" + routine2.actuatorId + "\t\t" + routine2.action + "\t" + routine2.comparation + "\t\t" + routine2.time + "\t" + hora);
        }
        System.out.println(i);
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
}
