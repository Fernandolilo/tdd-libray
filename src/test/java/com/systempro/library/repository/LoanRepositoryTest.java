package com.systempro.library.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.booleanThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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

		//cenario
		Book book = createNewBook("123");
		entityManager.persist(book);
		entityManager.flush();
		
		Loan loan = Loan.builder().book(book).customer("Fulano").instante(LocalDate.now()).build();

		entityManager.persist(loan);
		entityManager.flush();
		
		//execucao
		boolean exists =  repository.existsByBookAndNotReturned(book);

		//verificacao
		assertThat(exists).isTrue();
		
		}

	private  Book createNewBook(String isbn) {
		return Book.builder().autor("Fernando").title("As aventuras ").isbn(isbn).build();
	}
	}
