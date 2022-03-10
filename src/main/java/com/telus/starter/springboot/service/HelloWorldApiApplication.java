package com.telus.starter.springboot.service;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.ImportResource;
//import org.springframework.web.bind.annotation.RequestMapping;



@SpringBootApplication
//@ImportResource("classpath:spring/product-warranty-template-context.xml")
@ComponentScan(basePackages = { "com.telus.starter.springboot", "com.telus.api.security"})


public class HelloWorldApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(HelloWorldApiApplication.class, args);
	}

}

