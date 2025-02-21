package com.systempro.library.service.imp;

import com.systempro.library.entity.Loan;
import com.systempro.library.repository.LoanRepository;
import com.systempro.library.service.LoanService;

public class LoanServiceImp implements LoanService {
	
	private LoanRepository repository;

	public LoanServiceImp(LoanRepository repository) {
		this.repository = repository;
	}

	@Override
	public Loan save(Loan loan) {
		// TODO Auto-generated method stub
		return repository.save(loan);
	}

}
