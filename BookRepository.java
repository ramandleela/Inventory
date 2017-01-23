package com.bookstore.inventory.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.inventory.entity.BookRecord;

public interface BookRepository extends CrudRepository <BookRecord, Integer> {
	
}

