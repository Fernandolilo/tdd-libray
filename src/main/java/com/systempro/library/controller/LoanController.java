package com.systempro.library.controller;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.systempro.library.dto.LoanDTO;
import com.systempro.library.entity.Book;
import com.systempro.library.entity.Loan;
import com.systempro.library.service.BookService;
import com.systempro.library.service.LoanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/loans/")
@RequiredArgsConstructor
public class LoanController {

	private final LoanService service;
	private final BookService bookService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long create(@RequestBody LoanDTO dto) {
		Book book = bookService.getBookByIsbn(dto.getIsbn()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));

		Loan entity = Loan.builder().book(book).customer(dto.getCustomer()).instante(LocalDate.now()).build();

		entity = service.save(entity);

		return entity.getId();
	}

}
