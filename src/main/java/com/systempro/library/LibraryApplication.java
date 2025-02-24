
package com.systempro.library;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
//@EnableScheduling agendametno de tarefas
@EnableScheduling
@SpringBootApplication
public class LibraryApplication {
	
	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}
	
	//cron vem ce conologico ou sejatempo
	/*site da cronmaker para gerar os parametros cron
	 * 
	 * http://www.cronmaker.com/;jsessionid=node02bro7auu4cd31t3tt77fc01eb2146780.node0?0
	 * */

	@Scheduled(cron = "0 50 9 ? * MON-FRI")
	public void testAgendamentoTarefaz() {
		System.out.println("AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
