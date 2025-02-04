package com.systempro.library.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.systempro.library.entity.Book;
import com.systempro.library.exceptions.BusinessException;
import com.systempro.library.repository.BookRepository;

@Service
public class BookServiceImp implements BookService {

	private final BookRepository repository;

	public BookServiceImp(BookRepository repository) {
		this.repository = repository;
	}

	@Override
	public Book save(Book book) {
		if(repository.existsByIsbn(book.getIsbn()) ) {
			throw new BusinessException("ISBN ja cadastrado");
		}
		return repository.save(book);
	}

	@Override
	public Optional<Book> getById(Long id) {
		return Optional.empty();
	}

	@Override
	public void delete(Book book) {
		
	}

}
