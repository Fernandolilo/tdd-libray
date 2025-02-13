package com.systempro.library.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.library.dto.LoanDTO;
import com.systempro.library.entity.Book;
import com.systempro.library.entity.Loan;
import com.systempro.library.service.BookService;
import com.systempro.library.service.LoanService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanContollerTest {
	
	static String LOAN_API = "/loans/";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
    private BookService bookService;
	
	@MockBean
    private  LoanService loanService;
	
	@Test
	@DisplayName("Deve realizar um emprestimo")
	public void createLoanTets() throws Exception {
		//cenario
		
		LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();		
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		
		Book book = Book.builder().id(1L).isbn("123").build();
		
		BDDMockito.given( bookService.getBookByIsbn("123")).willReturn(Optional.of(book));
		
		
		Loan loan = Loan.builder()
				.id(1l)
				.customer("Fulano")
				.instante(LocalDate.now())
				.retunear(true)
				.book(book)
				.build();
		
		BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( LOAN_API )
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json);
		
		mockMvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(content().string("1") )
			;		
		
	}
	
	@Test
	@DisplayName("Deve retornar um erro ao tentar fazer um emprestimo de u livro inexistente")
	public void invalidisbnCreatedLoanTest() throws Exception {
		LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
		
		String json =  new ObjectMapper().writeValueAsString(dto);
		
		BDDMockito.given( bookService.getBookByIsbn( "123" )).willReturn(Optional.empty());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( LOAN_API )
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect( jsonPath("errors", Matchers.hasSize(1)) )
			.andExpect(jsonPath("errors[0]").value("Book not found for passed isbn"));
			;		
	}

}
