package com.systempro.library.service.imp;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.systempro.library.dto.LoanFilterDTO;
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

	@Override
	public Optional<Loan> getById(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
	public Loan update(Loan loan) {
		return repository.save(loan);
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<Loan> find(LoanFilterDTO filter, Pageable pageble) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
