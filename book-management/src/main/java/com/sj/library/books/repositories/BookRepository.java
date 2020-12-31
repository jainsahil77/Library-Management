package com.sj.library.books.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.sj.library.books.entities.Book;

/**
 * Repository for book
 * 
 * @author Sahil Jain
 *
 */
@Repository
@RepositoryRestResource(exported = false)
public interface BookRepository extends CrudRepository<Book, Integer> {
	/**
	 * Get list of all books present in DB
	 */
	List<Book> findAll();

	/**
	 * Get list of all books containing provided name string
	 * 
	 * @param bookName
	 * @return List of Book
	 */
	List<Book> findByBookNameContaining(String bookName);
}
