package com.systempro.library.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBeans {
	

	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}

}
