package com.systempro.library.service;

import java.util.Optional;

import com.systempro.library.entity.Book;

public interface BookService {

	Book save(Book any);

	Optional<Book> getById(Long id);

}
