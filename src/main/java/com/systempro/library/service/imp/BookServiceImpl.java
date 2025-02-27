package com.systempro.library.service.imp;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.systempro.library.entity.Book;
import com.systempro.library.exceptions.BusinessException;
import com.systempro.library.repository.BookRepository;
import com.systempro.library.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private final BookRepository repository;

	public BookServiceImpl(BookRepository repository) {
		this.repository = repository;
	}

	@Override
	public Book save(Book book) {
		if (repository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("ISBN ja cadastrado");
		}
		return repository.save(book);
	}

	@Override
	public Optional<Book> getById(Long id) {
		return this.repository.findById(id);
	}

	@Override
	public void delete(Book book) {

		if (book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}
		this.repository.delete(book);
	}

	@Override
	public Book update(Book book) {
		// TODO Auto-generated method stub
		if (book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}
		return this.repository.save(book);
	}

	@Override
	public Page<Book> find(Book filter, Pageable pageRequest) {
		// TODO Auto-generated method stub
		Example<Book> example =Example.of(filter, ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				
				);
		return repository.findAll(example, pageRequest);
	}

	@Override
	public Optional<Book> getBookByIsbn(String isbn) {
		// TODO Auto-generated method stub
		return repository.findByIsbn(isbn);
	}

}
