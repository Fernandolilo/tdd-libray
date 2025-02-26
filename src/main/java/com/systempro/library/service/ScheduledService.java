package com.systempro.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.systempro.library.entity.Loan;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduledService {

	/*
	 * //cron vem ce conologico ou sejatempo /*site da cronmaker para gerar os
	 * parametros cron 
	 * http://www.cronmaker.com/;jsessionid=node02bro7auu4cd31t3tt77fc01eb2146780.
	 * node0?0
	 */

	private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

	private final LoanService loanService;
	private final EmailService emailService;

	
	@Scheduled(cron = CRON_LATE_LOANS)
	public void sendMailToLateLoans() {
		
		 String message ="Devolva os livros mal carater;";		
		
		List<Loan> allLateLoans = loanService.getAllLateLoans();
		
		List<String> mails = allLateLoans.stream().map(
					loan -> loan.getCustomerEmail()
				).collect(Collectors.toList());
		

		emailService.sendEmail(message ,mails);
		
	}

}
