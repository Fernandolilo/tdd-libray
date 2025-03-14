package com.systempro.library.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.systempro.library.dto.BookDTO;
import com.systempro.library.dto.LoanDTO;
import com.systempro.library.entity.Book;
import com.systempro.library.entity.Loan;
import com.systempro.library.service.BookService;
import com.systempro.library.service.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "BOOKS")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

	private final BookService service;
	private final ModelMapper mapper;
	private final LoanService loanService;


	@Operation(summary = "CRIAÇÃO DE NOVO LIVRO")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody @Valid BookDTO dto) {
		Book entity = mapper.map(dto, Book.class);
		entity = service.save(entity);
		
		log.info("creating a book for isbn: {}", dto.getIsbn());
		return mapper.map(entity, BookDTO.class);
	}

	@Operation(summary = "BUSCA DE LIVRO POR ID")
	@GetMapping("/{id}")
	public BookDTO getById(@PathVariable Long id) {
		log.info(" obtaining details for boos id: {}", id);
		return service.getById(id).map(book -> mapper.map(book, BookDTO.class))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	}

	@Operation(summary = "DELETA LIVRO POR ID")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		log.info("deleting book of id: {}", id);
		Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		service.delete(book);
	}

	@Operation(summary = "ATUALIZA LIVRO POR ID")
	@PutMapping("/{id}")
	public BookDTO update(@PathVariable Long id, BookDTO dto) {

		Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		book.setAutor(dto.getAutor());
		book.setTitle(dto.getTitle());

		book = service.update(book);

		return mapper.map(book, BookDTO.class);
	}

	@Operation(summary = "BUSCA DE LIVRO PAGINADO")
	@GetMapping
	public Page<BookDTO> find(BookDTO dto, Pageable pageRequest) {
		Book filter = mapper.map(dto, Book.class);

		filter = Book.builder().autor("Fernando").title("As aventuras").isbn("001").build();

		Page<Book> result = service.find(filter, pageRequest);

		List<BookDTO> list = result.getContent().stream().map(entity -> mapper.map(entity, BookDTO.class))
				.collect(Collectors.toList());

		return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
	}

	@Operation(summary = "BUSCA DE LIVRO ID TRAZENDO EMPRESTIMO")	
	@GetMapping("/{id}/loan")
	public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable) {

		Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));		
		Page<Loan> result =loanService.getLoansByBook(book, pageable);
		List<LoanDTO> list = result.getContent()
			.stream()
			.map(loan ->{
				Book loanBook = loan.getBook();
				BookDTO bookDTO = mapper.map(loanBook, BookDTO.class);
				LoanDTO loanDTO = mapper.map(loan, LoanDTO.class);
				loanDTO.setBook(bookDTO);
				return loanDTO;
			}).collect(Collectors.toList());
		return new PageImpl<LoanDTO>(list,pageable, result.getTotalElements());

	}

}
