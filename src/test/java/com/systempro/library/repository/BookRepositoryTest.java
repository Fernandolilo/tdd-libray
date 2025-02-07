package com.systempro.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.library.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	BookRepository repository;

	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um livro na base com ISBN informado")
	public void returnTrueWhenIsbnExists() {

		// cenario
		String isbn = "123";
		Book book = createNewBook(isbn);
		entityManager.persist(book);

		// execução

		boolean exists = repository.existsByIsbn(isbn);
		// verificação

		assertThat(exists).isTrue();
	}

	@Test
	@DisplayName("Deve retornar false quando não existir um livro na base com ISBN informado")
	public void returnFalseWhenIsbnDoesExists() {

		// cenario
		String isbn = "123";

		// execução

		boolean exists = repository.existsByIsbn(isbn);
		// verificação

		assertThat(exists).isFalse();
	}

	// test de integração

	@Test
	@DisplayName("Deve obter um livro por ID")
	public void findByIdTest() {
		// cenario
		Book book = createNewBook("123");
		// neste caso para eu encontrar um book por id, preciso persistir ele primeiro.
		entityManager.persist(book);

		// exec
		Optional<Book> foundBook = repository.findById(book.getId());

		// verificacao

		assertThat(foundBook.isPresent()).isTrue();

	}

	
	private Book createNewBook(String isbn) {
		return Book.builder().autor("Fernando").title("As aventuras ").isbn(isbn).build();
	}
}
