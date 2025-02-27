package com.systempro.library.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.library.dto.BookDTO;
import com.systempro.library.entity.Book;
import com.systempro.library.exceptions.BusinessException;
import com.systempro.library.service.BookService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
public class BookControllerTests {

	static String BOOK_API = "/books";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	BookService service;

	@DisplayName("DEVE CRIAR UM LIVRO COM SUCESSO.")
	@Test
	public void createBookTest() throws Exception {

		BookDTO dto = createNewBook();

		// instacia de mock para test, salvar novo book, simular o save do service
		Book savedBook = Book.builder().id(1L).autor("Fernando").title("As aventuras").isbn("001").build();
		BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(request).andExpect(status().isCreated())
				.andExpect(jsonPath("id").value(1L))
				.andExpect(jsonPath("title").value(dto.getTitle()))
				.andExpect(jsonPath("autor").value(dto.getAutor()))
				.andExpect(jsonPath("isbn").value(dto.getIsbn()));

	}

	

	@DisplayName("DEVE lancar erro de validacao quando houver dados nulls.")
	@Test
	public void createInvalidBookTest() throws Exception {
		String json = new ObjectMapper().writeValueAsString(new BookDTO());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("errors", Matchers.hasSize(3)));
	}

	@DisplayName("DEVE lancar erro quando tentar salvar um livro com o ISBN ja cadastrado.")
	@Test
	public void createBookWithDuplicatedIsbn() throws Exception {
		
		BookDTO dto = createNewBook();		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(new BusinessException("ISBN ja cadastrado"));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		String messageEror = "ISBN ja cadastrado";
		
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors", Matchers.hasSize(1)))
				.andExpect(jsonPath("errors[0]").value(messageEror));
		
	}
	
	@Test
	@DisplayName("Deve obter informacoes de um livro")
	public void getBookDetailsTest() throws Exception {
		
		Long id = 1L;
		Book book = Book.builder().id(id)
				.title(createNewBook().getTitle())
				.autor(createNewBook().getAutor())
				.isbn(createNewBook().getIsbn())
				.build();
		
		BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));
		
		//execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/"+id))
		.accept(MediaType.APPLICATION_JSON);
		
		mockMvc
			.perform(request)
			.andExpect(status().isOk() )
			.andExpect(jsonPath("id").value(id))
			.andExpect(jsonPath("title").value(createNewBook().getTitle()))
			.andExpect(jsonPath("autor").value(createNewBook().getAutor()))
			.andExpect(jsonPath("isbn").value(createNewBook().getIsbn()));

			
	}
	
	@Test
	@DisplayName("Deve retornar resource not found quando o livro procurado não existir")
	public void bookNotFoundTest() throws Exception {
		
		
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());
		
		//execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/"+ 1))
		.accept(MediaType.APPLICATION_JSON);
		
		mockMvc
		.perform(request)
		.andExpect(status().isNotFound() );
		
	}
	
	@Test
	@DisplayName("Deve deletar um book")
	public void deleteBookTest() throws Exception {
		
		BDDMockito.given(service.getById(anyLong())).willReturn(Optional.of(Book.builder().id(1L).build()));
		//execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BOOK_API.concat("/"+ 1))
		.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request) 
			.andExpect( status().isNoContent());
		
	}
	
	
	@Test
	@DisplayName("Deve retormar resource not found ao deletar um book inexistente")
	public void deleteBookNotFoundTest() throws Exception {		
		BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());
		//execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BOOK_API.concat("/"+ 1))
				
		.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request) 
			.andExpect( status().isNotFound());
		
	}
	
	@Test
	@DisplayName("Deve atualizar um book")
	public void updateBookTest() throws Exception {		
		Long id = 1L;
		String json =new ObjectMapper().writeValueAsString(createNewBook());
		
		//busca um livro no servior por um mock
		Book updatingBook = Book.builder().id(1L).title("some title").autor("some autor").isbn("001").build();
		
		BDDMockito.given(service.getById(id)).willReturn(Optional.of(updatingBook));
		
		Book bookUpdateBuilder = Book.builder().id(id).autor("Fernando").title("As aventuras").isbn("001").build();
		
		BDDMockito.given(service.update(updatingBook)).willReturn(bookUpdateBuilder);
		
		//execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(BOOK_API.concat("/"+ 1))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
		;
		
		mockMvc.perform(request) 
			.andExpect( status().isOk())
			.andExpect(jsonPath("id").value(id))
			.andExpect(jsonPath("title").value(createNewBook().getTitle()))
			.andExpect(jsonPath("autor").value(createNewBook().getAutor()))
			.andExpect(jsonPath("isbn").value(createNewBook().getIsbn()));
	}
	
	@Test
	@DisplayName("Deve retornar um 404 ao tentar atualizar um book inexistente")
	public void updateBookInexistsTest() throws Exception {
		
		String json =new ObjectMapper().writeValueAsString(createNewBook());
		
		
		
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());
		//execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(BOOK_API.concat("/"+ 1))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(json)
		;
		
		mockMvc.perform(request) 
			.andExpect( status().isNotFound());
		
	}
	
	
	@Test
	@DisplayName("deve filtrar livros")
	public void findBooksTests() throws Exception {
		
		//cenario
		Long id = 1L;
		
		Book book = Book.builder()
				.id(id)
				.title(createNewBook().getTitle() )
				.autor(createNewBook().getAutor() )
				.isbn(createNewBook().getIsbn())
				.build();
		
		// Definindo comportamento do mock
		BDDMockito.given(service.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
	    .willReturn(new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1L));

        
        
        
        //  String queryString = String.format("?title=%s&autor=%s&page=0&size=100", book.getTitle(), book.getAutor());
		//estamos fazendo uma interpolação, ?title=% == book.getTitle(), &autor=%s == book.getAutor());
        
		String queryString = String.format("?title=%s&autor=%s&page=0&size=100", book.getTitle(), book.getAutor());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        	.get(BOOK_API.concat(queryString))
        	.accept(MediaType.APPLICATION_JSON);
        
        mockMvc.perform(request)
        .andExpect( status().isOk())
        .andExpect( jsonPath("content", Matchers.hasSize(1)))
        .andExpect( jsonPath("totalElements").value(1) )
        .andExpect( jsonPath("pageable.pageSize").value(100))
        .andExpect( jsonPath("pageable.pageNumber").value(0))
        
        ;
	}
	
	private BookDTO createNewBook() {
		return BookDTO.builder().autor("Fernando").title("As aventuras").isbn("001").build();
	}
}
