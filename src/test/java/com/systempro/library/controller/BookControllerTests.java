package com.systempro.library.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
@WebMvcTest
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
		Book savedBook = Book.builder().id("1L").author("Fernando").title("As aventuras ").isbn("001").build();
		BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(request).andExpect(status().isCreated()).andExpect(jsonPath("id").value("1L"))
				.andExpect(jsonPath("title").value(dto.getTitle())).andExpect(jsonPath("author").value(dto.getAuthor()))
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
	
	
	
	private BookDTO createNewBook() {
		return BookDTO.builder().author("Fernando").title("As aventuras ").isbn("001").build();
	}
}
