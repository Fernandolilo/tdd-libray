package com.systempro.library.config;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import com.systempro.library.service.EmailService;
import com.systempro.library.service.imp.EmailServiceImpl;

@Configuration
public class ConfigBens {
	
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

}
