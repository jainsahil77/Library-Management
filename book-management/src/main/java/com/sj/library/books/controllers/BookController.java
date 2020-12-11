package com.sj.library.books.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sj.library.books.entities.Book;
import com.sj.library.books.services.BookService;

/**
 * Rest Controller for exposing REST APIs for books
 * 
 * @author Sahil Jain
 *
 */
@RestController
@RequestMapping(value = "/books-management")
public class BookController {
	@Autowired
	private BookService bookService;

	/**
	 * Get list of all books
	 * 
	 * @return ResponseEntity with list of all books
	 */
	@GetMapping(value = "/books")
	public ResponseEntity<List<Book>> getAllBooks() {
		return bookService.getAllBooks();
	}

	/**
	 * Get Book entity by ID
	 * 
	 * @param id - book ID
	 * @return ResponseEntity with book entity
	 */
	@GetMapping(value = "/bookByID/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable(name = "id", required = true) Integer id) {
		return bookService.getBookById(id);
	}

	/**
	 * Get list of books containing given book name
	 * 
	 * @param name - book name
	 * @return ResponseEntity with list of books
	 */
	@GetMapping(value = "/bookByName/{name}")
	public ResponseEntity<List<Book>> getBookByName(@PathVariable(name = "name", required = true) String name) {
		return bookService.getBookByName(name);
	}

	/**
	 * Issue book
	 * 
	 * @param id - Book ID
	 * @return ResponseEntity with description
	 */
	@PatchMapping(value = "/issue/{id}")
	public ResponseEntity<String> issueBook(@PathVariable(name = "id", required = true) Integer id) {
		return bookService.issueBookById(id);
	}

	/**
	 * Return book
	 * 
	 * @param id - Book ID
	 * @return ResponseEntity with description
	 */
	@PatchMapping(value = "/return/{id}")
	public ResponseEntity<String> returnBook(@PathVariable(name = "id", required = true) Integer id) {
		return bookService.returnBookById(id);
	}

	/**
	 * Add new book
	 * 
	 * @param book - Book entity
	 * @return ResponseEntity with description
	 */
	@PutMapping(value = "/add")
	public ResponseEntity<String> addNewBook(@Valid @RequestBody(required = true)  Book book) {
		return bookService.saveBook(book);
	}

	/**
	 * Remove book by ID
	 * 
	 * @param id - Book ID
	 * @return ResponseEntity with description
	 */
	@DeleteMapping(value = "/remove/{id}")
	public ResponseEntity<String> removeBook(@PathVariable(name = "id", required = true) Integer id) {
		return bookService.removeBook(id);
	}
}
