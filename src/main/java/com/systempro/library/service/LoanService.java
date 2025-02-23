package com.systempro.library.service;

import java.util.Optional;

import com.systempro.library.entity.Loan;

public interface LoanService {
	
	Loan save(Loan loan);

	Optional<Loan> getById(Long id);

	Loan update(Loan loan);

}
