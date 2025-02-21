package com.systempro.library.service;

import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.repository.support.Repositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.library.entity.Book;
import com.systempro.library.entity.Loan;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {
	
	private  LoanRepository  repository;

	@Test
	@DisplayName("Deve Salvar um emprestimo")
	public void saveLoanTest() {
		Book book = Book.builder().id(1L).build();
		String customer = "Fulano";
		
		Loan loan  = Loan.builder()
				.book(book)
				.customer(customer)
				.instante(LocalDate.now())
				.build();
		
		Loan savedLoan = Loan.builder()
				.id(1L)
				.instante(LocalDate.now())
				.customer(customer)
				.book(book).build();

		when( repository.save(loan)).thenReturn(savedLoan);
	}
}
