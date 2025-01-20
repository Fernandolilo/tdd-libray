package com.systempro.library.service;

import org.springframework.stereotype.Service;

import com.systempro.library.entity.Book;
import com.systempro.library.repository.BookRepository;

@Service
public class BookServiceImp implements BookService {

	private final BookRepository repository;

	public BookServiceImp(BookRepository repository) {
		this.repository = repository;
	}

	@Override
	public Book save(Book book) {
		// TODO Auto-generated method stub
		return repository.save(book);
	}

}
