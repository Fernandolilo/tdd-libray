package com.systempro.library.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTests {
	
	static String BOOK_API =  "/books";
	
	final MockMvc mockMvc;
	

	public BookControllerTests(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@DisplayName("DEVE CRIAR UM LIVRO COM SUCESSO.")
	@Test
	public void createBookTest() throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(null);
	
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
						.post(BOOK_API)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json);
						
		
		mockMvc
			.perform(request)
			.andExpect(status().isCreated() )
			.andExpect(jsonPath("id").isNotEmpty() )
			.andExpect(jsonPath("title").value("Meu livro") )
			.andExpect(jsonPath("author").value("Autor") )
			.andExpect(jsonPath("isbn").value("123123") )
			;
		
	}
	
	@DisplayName("DEVE lancar erro de validacao quando houver dados nulls.")
	@Test
	public void createInvalidBookTest() {
		
	}


}
