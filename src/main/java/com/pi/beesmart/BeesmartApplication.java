package com.pi.beesmart;

import com.pi.beesmart.controller.DeviceController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeesmartApplication {
	public static void main(String[] args) throws InterruptedException {
		DeviceController deviceController = new DeviceController();
		SpringApplication.run(BeesmartApplication.class, args);
		deviceController.getInPin();
	}
}
