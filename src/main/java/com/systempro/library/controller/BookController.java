package com.systempro.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.library.dto.BookDTO;
import com.systempro.library.entity.Book;
import com.systempro.library.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	private final BookService service;

	public BookController(BookService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO dto) {
		Book entity = Book.builder()
				.author(dto.getAuthor())
				.title(dto.getTitle())
				.isbn(dto.getIsbn())
				.build();
		entity = service.save(entity);

		return BookDTO.builder()
				.id(entity.getId())
				.author(entity.getAuthor())
				.title(entity.getTitle())
				.isbn(entity.getIsbn())
				.build();
	}
}
