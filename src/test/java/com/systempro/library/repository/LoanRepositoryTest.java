package com.systempro.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.library.entity.Book;
import com.systempro.library.entity.Loan;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

	@Autowired
	private LoanRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("Deve verificar se existe um emprestimo para um livro")
	public void existsByBookAndNotReturnedTest() {
		// cenario
		Loan loan =	createPersistBookAndLoan(LocalDate.now());
		Book book = loan.getBook();

		// execucao
		boolean exists = repository.existsByBookAndNotReturned(book);

		// verificacao
		assertThat(exists).isTrue();

	}
	
	@Test 
	@DisplayName("Deve buscar um imprestimo pelo customer ou isbn")
	public void findByBookIsbnOrCustomerTest() {
		
		//cenario
		createPersistBookAndLoan(LocalDate.now());
		
		//exec
		Page<Loan> result = repository.findByBookIsbnOrCustomer("321", "Fulano", PageRequest.of(0, 10));
		
		//vericacações
		
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
		assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(result.getTotalElements()).isEqualTo(1);
		
	}
	
	@Test
	@DisplayName("Deve obter dados de emprestimos cujo data de emprestimos for menor ou igrual a tres dias atras r não retornaram")	
	public void findByLoanDateLessThanAndNotRetuned() {
		
		Loan loan = createPersistBookAndLoan(LocalDate.now().minusDays(5));
		
		List<Loan> result = repository.findByLoanDateLessThanAndNotRetuned(LocalDate.now().minusDays(4));
		
		assertThat(result).hasSize(1).contains(loan);
		
	}
	
	@Test
	@DisplayName("Deve deve retornar vazio quando não houver emprestimos")	
	public void notFindByLoanDateLessThanAndNotRetuned() {
		
		Loan loan = createPersistBookAndLoan(LocalDate.now());
		
		List<Loan> result = repository.findByLoanDateLessThanAndNotRetuned(LocalDate.now().minusDays(4));
		
		assertThat(result).isEmpty();
		
	}
	
	public Loan createPersistBookAndLoan(LocalDate localDate) {
		Book book = createNewBook("123");
		entityManager.persist(book);
		entityManager.flush();

		Loan loan = Loan.builder().book(book).customer("Fulano").instante(localDate).build();

		entityManager.persist(loan);
		entityManager.flush();
		return loan;
	}
	
	

	private Book createNewBook(String isbn) {
		return Book.builder().autor("Fernando").title("As aventuras ").isbn(isbn).build();
	}
}
