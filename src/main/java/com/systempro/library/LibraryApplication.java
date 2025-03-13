
package com.systempro.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling agendametno de tarefas
@EnableScheduling
@SpringBootApplication
public class LibraryApplication {

	
	
   
	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
