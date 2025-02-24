
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

	@Autowired
	private EmailService emailService;
	
    @Bean
    public CommandLineRunner runner() {
        return args -> {
            List<String> emails = Arrays.asList("nando.systempro@hotmail.com");
            emailService.sendEmail("Testando Servico de emails", emails);
            System.out.println("Emails enviados........................");
        };
    }
    
    @Bean
    public EmailService emailService(JavaMailSender javaMailSender) {
        return new EmailServiceImpl(javaMailSender);
    }
	
	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
