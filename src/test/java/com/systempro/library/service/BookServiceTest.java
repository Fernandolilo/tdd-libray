package com.systempro.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.library.entity.Book;
import com.systempro.library.exceptions.BusinessException;
import com.systempro.library.repository.BookRepository;
import com.systempro.library.service.imp.BookServiceImp;

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

		// usar o Mocktio para simular um salvamento, passando uma string qualquer e
		// passando o default de boolean é false
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

		Mockito.when(repository.save(book))
				.thenReturn(Book.builder().id(1L).title("As aventuras").autor("Fulano").isbn("001").build());

		// execução

		Book savedBook = service.save(book);

		// verificação

		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAutor()).isEqualTo("Fulano");
		assertThat(savedBook.getIsbn()).isEqualTo("001");

	}

	@Test
	@DisplayName("Deve lancar um erro de negocio ao tentar salvar um livro com isbn ja cadastrado")
	public void shouldNotSaveABookWintDuplicatedISBN() {

		// cenario
		Book book = createValidBook();
		// usar o Mocktio para simular um salvamento, passando uma string qualquer e
		// dizendo que é verdadeiro uma vez que o default de boolean é false
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

		// "ISBN ja cadastrado"

		// execução
		Throwable exceptions = Assertions.catchThrowable(() -> service.save(book));

		// verificação
		assertThat(exceptions).isInstanceOf(BusinessException.class).hasMessage("ISBN ja cadastrado");

		// verifique que meu repository nunca seja chamado para salvar o save com o
		// paramento book
		Mockito.verify(repository, Mockito.never()).save(book);

	}

	@Test
	@DisplayName("Buscar por ID")
	public void findByIdTest() {

		// cenario
		Long id = 1L;
		Book book = createValidBook();
		book.setId(id);
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

		// exec

		Optional<Book> foundBook = service.getById(id);

		// verificaçoes

		assertThat(foundBook.isPresent()).isTrue();
		assertThat(foundBook.get().getId()).isEqualTo(id);
		assertThat(foundBook.get().getAutor()).isEqualTo(book.getAutor());
		assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
		assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());

	}

	@Test
	@DisplayName("Deve retornar vazio ao buscar um livro pro ID quando nãoexistir")
	public void notFoundByIdTest() {

		// cenario
		Long id = 1L;
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

		// exec
		Optional<Book> foundBook = service.getById(id);

		// verificaçoes

		assertThat(foundBook.isPresent()).isFalse();

	}

	@Test
	@DisplayName("Deve deletar um book")
	public void deleteBookTest() {
		// cenario
		Book book = Book.builder().id(1L).build();

		// exec

		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book));

		// verificacoes
		Mockito.verify(repository, Mockito.times(1)).delete(book);

	}

	@Test
	@DisplayName("Deve ocorrer um erro ao tentar deletar um book inexistente")
	public void invalidDeleteBookTest() {
		// cenario
		Book book = new Book();

		org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

		// verificacoes
		Mockito.verify(repository, Mockito.never()).delete(book);

	}
	
	@Test
	@DisplayName("Deve ocorrer um erro ao tentar atualizar um livro inexistente")
	public void invalidUpdateBookTest() {
		// cenario
		Book book = new Book();

		org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

		// verificacoes
		Mockito.verify(repository, Mockito.never()).save(book);

	}
	
	@Test
	@DisplayName("Deve atualizar um livro")
	public void updateBookTest() {
		//cenario
		Long id =1L;
	
		//livro a atualizar
		Book upateingBook = Book.builder().id(id).build();
		
		//simulação
		Book  updateBook = createValidBook();
		
		updateBook.setId(id);
		
		Mockito.when(repository.save(upateingBook)).thenReturn(updateBook);
		
		Book  book= service.update(upateingBook);
		
		//verificalçoes
		
		assertThat(book.getId()).isEqualTo(updateBook.getId());
		assertThat(book.getAutor()).isEqualTo(updateBook.getAutor());
		assertThat(book.getTitle()).isEqualTo(updateBook.getTitle());
		assertThat(book.getIsbn()).isEqualTo(updateBook.getIsbn());
		
	}
	
	
	@Test
	@DisplayName("Deve filtrar livro pela propriedade")
	public void findBookTest() {
		//cenario
		Book book = createValidBook();
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		List<Book> bookList = new ArrayList<Book>(); // Lista do tipo correto

		Page<Book> page = new PageImpl<>(bookList, pageRequest, 1);
		
		//Page<Book> pages = new PageImpl<Book>( Arrays.asList(book), PageRequest.of(0, 10), 1);
		
		Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
		   .thenReturn(page);
		
		//execução
		Page<Book> result = service.find(book, pageRequest);
		
		//verificação
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).isEqualTo(bookList);
		assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);

	}
	@Test
	@DisplayName("Deve obter um livro pelo isbn")
	public void getBookIsbnTest() {
		
		//cenario
		String isbn = "123";		
		when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1L).isbn(isbn).build()));
		
		//exec
		Optional<Book> request = service.getBookByIsbn(isbn);
		
		//verificacaos
		assertThat(request.isPresent());
		
		assertThat(request.get().getId()).isEqualTo(1L);
		assertThat(request.get().getIsbn()).isEqualTo(isbn);

		verify(repository, times(1)).findByIsbn(isbn);
		
		
	}
	
	
	private Book createValidBook() {
		return Book.builder().autor("Fulano").title("As aventuras").isbn("001").build();
	}
}
