package com.systempro.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.refEq;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.library.entity.Book;
import com.systempro.library.exceptions.BusinessException;
import com.systempro.library.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	@MockBean
	BookRepository repository;

	BookService service;

	@BeforeEach // @BeforeEach faz com que este metodo seja execultado antes de qualquer teste
				// em minha class
	public void setUp() {
		this.service = new BookServiceImp(repository);
	}

	@Test
	@DisplayName("Deve Salvar um livro")
	public void saveBookTest() {
		// cenario
		Book book = createValidBook();

		Mockito.when(repository.save(book))
				.thenReturn(Book.builder().id("1").title("As aventuras").author("Fulano").isbn("001").build());

		// execução

		Book savedBook = service.save(book);

		// verificação

		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		assertThat(savedBook.getIsbn()).isEqualTo("001");

	}

	@Test
	@DisplayName("Deve lancar um erro de negocio ao tentar salvar um livro com isbn ja cadastrado")
	public void shouldNotSaveABookWintDuplicatedISBN() {

		// cenario
		Book book = createValidBook();
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

		// "ISBN ja cadastrado"

		// execução
		Throwable exceptions = Assertions.catchThrowable(() -> service.save(book));
	
		//verificação
		assertThat(exceptions).isInstanceOf(BusinessException.class).hasMessage("ISBN ja cadastrado");
		
		//verifique que meu repository nunca seja chamado para salvar o save com o paramento book
		Mockito.verify(repository, Mockito.never()).save(book);

	}

	private Book createValidBook() {
		return Book.builder().author("Fulano").title("As aventuras").isbn("001").build();
	}
}
