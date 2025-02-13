package com.systempro.library.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.library.dto.LoanDTO;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanContollerTest {
	
	static String BOOK_API = "/loan";

	@Autowired
	private MockMvc mockMvc;

	
	@Test
	@DisplayName("Deve realizar um emprestimo")
	public void createLoanTets() {
		
		LoanDTO dto;
		String json = new ObjectMapper().writeValueAsString(dto);
		
	}

}
