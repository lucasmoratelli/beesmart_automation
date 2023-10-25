package com.pi.beesmart;

import com.pi.beesmart.controller.DeviceController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeesmartApplication {
	DeviceController deviceController = new DeviceController();
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(BeesmartApplication.class, args);

	}
	public void cyclic() throws InterruptedException {
		Thread.sleep(1000);
		deviceController.getInPin();
	}

}
