package com.systempro.library.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.systempro.library.dto.BookDTO;
import com.systempro.library.dto.LoanDTO;
import com.systempro.library.dto.LoanFilterDTO;
import com.systempro.library.dto.ReturnedLoanDTO;
import com.systempro.library.entity.Book;
import com.systempro.library.entity.Loan;
import com.systempro.library.service.BookService;
import com.systempro.library.service.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "LOANS")
@RestController
@RequestMapping("/loans/")
@RequiredArgsConstructor
public class LoanController {

	private final LoanService service;
	private final BookService bookService;
	private final ModelMapper mapper;

	@Operation(summary = "CRIAÇÃO DE NOVO EMPRESTIMO")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long create(@RequestBody LoanDTO dto) {
		Book book = bookService.getBookByIsbn(dto.getIsbn()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));

		Loan entity = Loan.builder().book(book).customer(dto.getCustomer()).instante(LocalDate.now()).build();

		entity = service.save(entity);

		return entity.getId();
	}
	
	@Operation(summary = "ATUALIZAÇÃO DE EMPRESTIMO")
	@PatchMapping("/{id}")
	public void returnedBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {
		
		Loan loan = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		loan.setReturned(dto.isReturned());
		service.update(loan);
	}

	@Operation(summary = "BUSCA PAGINADA DE EMPRESTIMOS")	
	@GetMapping
	public Page<LoanDTO> find (LoanFilterDTO dto, Pageable pageRequest){
		
		Page<Loan> result = service.find(dto, pageRequest);		
		 List<LoanDTO> loans =  result
			.getContent()
			.stream()
			.map(entity -> {
				Book book = entity.getBook();
				BookDTO  bookDTO = mapper.map(book, BookDTO.class);
				LoanDTO loanDTO = mapper.map(entity, LoanDTO.class);
				loanDTO.setBook(bookDTO);
				return loanDTO;
			}).collect(Collectors.toList());
		 
		 return new PageImpl<LoanDTO>(loans, pageRequest, result.getTotalElements());
		
		
	}
	
}
