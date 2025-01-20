package com.systempro.library.service;



import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.library.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
	
	BookService service;
	
	@Test
	@DisplayName("Deve Salvar um livro")
	public void saveBookTest() {
		//cenario
		Book book = Book.builder().author("Fulano").title("As aventuras").isbn("001").build();
		
		//execução
		
		Book savedBook = service.save(book);
		
		//verificação
		
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		assertThat(savedBook.getIsbn()).isEqualTo("001");
		
	}

}
