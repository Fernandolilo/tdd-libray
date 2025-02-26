
package com.systempro.library;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.systempro.library.service.EmailService;
import com.systempro.library.service.imp.EmailServiceImpl;

//@EnableScheduling agendametno de tarefas
@EnableScheduling
@SpringBootApplication
public class LibraryApplication {

	
	
   
	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
