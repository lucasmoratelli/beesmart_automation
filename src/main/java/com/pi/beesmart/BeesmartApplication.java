package com.pi.beesmart;

import com.pi.beesmart.controller.device.DeviceController;
import com.pi.beesmart.controller.routine.RoutineController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class BeesmartApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(BeesmartApplication.class, args);

		//Chama Método de Rotina
		RoutineController routineController = new RoutineController();
		routineController.executeRoutine();
	}
}
