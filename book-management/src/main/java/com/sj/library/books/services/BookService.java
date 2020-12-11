package com.sj.library.books.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sj.library.books.entities.Book;
import com.sj.library.books.repositories.BookRepository;

/**
 * Service for managing book services like save, remove, issue, return and get
 * 
 * @author Sahil Jain
 *
 */
@Service
public class BookService {
	private static final String BOOK_NOT_FOUND = "Book not found";

	@Autowired
	private BookRepository bookRepository;

	/**
	 * Save book entity
	 * 
	 * @param book - Book entity
	 * @return ResponseEntity with description
	 */
	public ResponseEntity<String> saveBook(Book book) {
		if (Objects.nonNull(book)) {
			if (bookRepository.existsById(book.getBookId()))
				return new ResponseEntity<>("Book already present", HttpStatus.UNPROCESSABLE_ENTITY);
			bookRepository.save(book);
			return new ResponseEntity<>("Book saved", HttpStatus.CREATED);
		} else
			return new ResponseEntity<>("Book not provided", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Remove book by ID
	 * 
	 * @param id - Book ID
	 * @return ResponseEntity with description
	 */
	public ResponseEntity<String> removeBook(Integer id) {
		if (Objects.nonNull(id)) {
			if (!bookRepository.existsById(id))
				return new ResponseEntity<>(BOOK_NOT_FOUND, HttpStatus.NOT_FOUND);
			bookRepository.deleteById(id);
			return new ResponseEntity<>("Book deleted", HttpStatus.OK);
		} else
			return new ResponseEntity<>("Book ID cannot be empty", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Get all books
	 * 
	 * @return ResponseEntity with list of books
	 */
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> allBooks = bookRepository.findAll();
		if (Objects.nonNull(allBooks) && !allBooks.isEmpty())
			return new ResponseEntity<>(allBooks, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get book by ID
	 * 
	 * @param id - Book ID
	 * @return ResponseEntity with book entity
	 */
	public ResponseEntity<Book> getBookById(Integer id) {
		Optional<Book> optional = bookRepository.findById(id);
		if (optional.isPresent())
			return new ResponseEntity<>(optional.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get list of all books containing given book name
	 * 
	 * @param bookName - Book name
	 * @return ResponseEntity with list of books
	 */
	public ResponseEntity<List<Book>> getBookByName(String bookName) {
		List<Book> bookList = bookRepository.findByBookNameContaining(bookName);
		if (Objects.nonNull(bookList) && !bookList.isEmpty())
			return new ResponseEntity<>(bookList, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Issue book by book ID
	 * 
	 * @param id - Book ID
	 * @return ResponseEntity with description
	 */
	public ResponseEntity<String> issueBookById(Integer id) {
		Optional<Book> optional = bookRepository.findById(id);
		if (optional.isPresent()) {
			Book book = optional.get();
			int availableCopies = book.getAvailableCopies();
			if (availableCopies > 0) {
				book.setAvailableCopies(--availableCopies);
				bookRepository.save(book);
				return new ResponseEntity<>("Book Issued", HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<>("All copies are already issued", HttpStatus.UNPROCESSABLE_ENTITY);
			}
		} else
			return new ResponseEntity<>(BOOK_NOT_FOUND, HttpStatus.NOT_FOUND);
	}

	/**
	 * Return book by Book ID
	 * 
	 * @param id - Book ID
	 * @return ResponseEntity with description
	 */
	public ResponseEntity<String> returnBookById(Integer id) {
		Optional<Book> optional = bookRepository.findById(id);
		if (optional.isPresent()) {
			Book book = optional.get();
			int availableCopies = book.getAvailableCopies();
			if (availableCopies < book.getTotalCopies()) {
				book.setAvailableCopies(++availableCopies);
				bookRepository.save(book);
				return new ResponseEntity<>("Book Returned", HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<>("Invalid return. All copies of given ID are already returned.", HttpStatus.UNPROCESSABLE_ENTITY);
			}
		} else
			return new ResponseEntity<>(BOOK_NOT_FOUND, HttpStatus.NOT_FOUND);
	}
}
