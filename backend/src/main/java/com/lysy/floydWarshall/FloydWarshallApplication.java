package com.lysy.floydWarshall;

import com.lysy.floydWarshall.controller.StructureController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = StructureController.class)
public class FloydWarshallApplication {

	public static void main(String[] args) {
		SpringApplication.run(FloydWarshallApplication.class, args);
	}

}
