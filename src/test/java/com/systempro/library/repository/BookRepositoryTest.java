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

	@Test
	@DisplayName("Deve salvar um livro")
	public void savedBook() {
		// cenario
		Book book = createNewBook("123");

		Book saveBook = repository.save(book);

		assertThat(saveBook.getId()).isNotNull();

	}

	@Test
	@DisplayName("Deve deletar um livro")
	public void deleteBook() {
		// cenario
		Book book = createNewBook("123");
		//preciso persistir ele primeiro.
		entityManager.persist(book);

		//buscar o livro
		Book founfBook = entityManager.find(Book.class, book.getId());

		//deletar o livro 
		repository.delete(founfBook);

		//verificar se foi deletado
		Book deleteBook = entityManager.find(Book.class, book.getId());
		
		//assegurar que esta nulo com a deleção, posso fazer desta forma ou a forma de baixo que ja economiza linha de cod;
		assertThat(deleteBook).isNull();
		
		assertThat(entityManager.find(Book.class, book.getId())).isNull();
	}

	private Book createNewBook(String isbn) {
		return Book.builder().autor("Fernando").title("As aventuras ").isbn(isbn).build();
	}
}
