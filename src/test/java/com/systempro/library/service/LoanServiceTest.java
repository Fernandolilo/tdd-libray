package com.systempro.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.library.entity.Book;
import com.systempro.library.entity.Loan;
import com.systempro.library.repository.LoanRepository;
import com.systempro.library.service.imp.LoanServiceImp;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {
	
	private LoanService service;

	@MockBean
	private  LoanRepository  repository;
	
	@BeforeEach
	public void setUp() {
		this.service = new LoanServiceImp(repository);
	}
	
	@Test
	@DisplayName("Deve Salvar um emprestimo")
	public void saveLoanTest() {
		Book book = Book.builder().id(1L).build();
		String customer = "Fulano";
		
		Loan savingLoan  = Loan.builder()
				.book(book)
				.customer(customer)
				.instante(LocalDate.now())
				.build();
		
		Loan savedLoan = Loan.builder()
				.id(1L)
				.instante(LocalDate.now())
				.customer(customer)
				.book(book).build();

		when( repository.save(savingLoan)).thenReturn(savedLoan);
		
		Loan loan = service.save(savingLoan);
		
		assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
		assertThat(loan.getInstante()).isEqualTo(savedLoan.getInstante());

		assertThat(loan.getId()).isEqualTo(savedLoan.getId());

		
	}
}
