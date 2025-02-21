package com.systempro.library.service.imp;

import org.springframework.stereotype.Service;

import com.systempro.library.entity.Loan;
import com.systempro.library.exceptions.BusinessException;
import com.systempro.library.repository.LoanRepository;
import com.systempro.library.service.LoanService;

@Service
public class LoanServiceImpl implements LoanService {
	
	private LoanRepository repository;

	public LoanServiceImpl(LoanRepository repository) {
		this.repository = repository;
	}

	@Override
	public Loan save(Loan loan) {
		
		if(repository.existsByBookAndNotReturned(loan.getBook()) ) {
			throw new BusinessException("Book already loaned");
		}
		return repository.save(loan);
	}
	

}
