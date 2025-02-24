package com.systempro.library.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.systempro.library.dto.LoanFilterDTO;
import com.systempro.library.entity.Loan;

public interface LoanService {
	
	Loan save(Loan loan);

	Optional<Loan> getById(Long id);

	Loan update(Loan loan);

	Page<Loan> find(LoanFilterDTO filter, Pageable pageble);

}
