package com.pi.beesmart;

import com.pi.beesmart.controller.device.DeviceController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeesmartApplication {
	public static void main(String[] args) {
		DeviceController deviceController = new DeviceController();
		SpringApplication.run(BeesmartApplication.class, args);
		deviceController.getInPin();
	}
}
