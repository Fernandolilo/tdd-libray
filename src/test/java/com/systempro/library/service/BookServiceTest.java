package com.systempro.library.service;



import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.library.entity.Book;
import com.systempro.library.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
	
	@MockBean
	BookRepository repository;
	
	BookService service;
	
	@BeforeEach //@BeforeEach faz com que este metodo seja execultado antes de qualquer teste em minha class
	public void setUp() {		
		this.service = new BookServiceImp(repository);		
	}
	
	@Test
	@DisplayName("Deve Salvar um livro")
	public void saveBookTest() {
		//cenario
		Book book = Book.builder().author("Fulano").title("As aventuras").isbn("001").build();
		
		Mockito.when(repository.save(book)).thenReturn(Book.builder().id("1").title("As aventuras").author("Fulano").isbn("001").build());
		
		//execução
		
		Book savedBook = service.save(book);
		
		//verificação
		
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		assertThat(savedBook.getIsbn()).isEqualTo("001");
		
	}

}
