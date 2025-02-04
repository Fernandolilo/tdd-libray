package com.systempro.library.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import com.systempro.library.entity.Book;
import com.systempro.library.exceptions.BusinessException;
import com.systempro.library.exceptions.apierror.ApiErrors;
import com.systempro.library.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {

	private final BookService service;
	private final ModelMapper mapper;

	public BookController(BookService service, ModelMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody @Valid BookDTO dto) {
		Book entity =  mapper.map(dto, Book.class);
		entity = service.save(entity);
		return mapper.map(entity, BookDTO.class);
	}
	
	
	
	@GetMapping("/{id}")
	public BookDTO getById(@PathVariable Long id) {
		return service
				.getById(id)
				.map(book -> mapper.map(book, BookDTO.class) )
				.orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND))
				;
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		
		Book book = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		service.delete(book);
	}
	
	@PutMapping("/{id}")
	public BookDTO update(@PathVariable Long id, BookDTO dto) {
		
		Book book = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		book.setAutor(dto.getAutor());
		book.setTitle(dto.getTitle());
		
		service.update(book);
		
		return mapper.map(book, BookDTO.class);
	}
	
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationsExceptions(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();		
		return new ApiErrors(bindingResult);
	}
	
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handlerBusinessExceptions(BusinessException ex) {
		//BindingResult bindingResult = ex.getBindingResult();		
		return new ApiErrors(ex);
	}
	
}
