package com.sj.library.books.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sj.library.books.entities.Book;
import com.sj.library.books.repositories.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

	@InjectMocks
	private BookService bookService;

	@Mock
	private BookRepository bookRepository;

	private Book book;
	private List<Book> listBook;

	@BeforeEach
	public void setup() {
		book = new Book();
		book.setAuthorName("author");
		book.setAvailableCopies(5);
		book.setBookId(1);
		book.setBookName("book");
		book.setTotalCopies(7);
		listBook = new LinkedList<>();
		listBook.add(book);
	}

	@DisplayName("Test get all Books - OK")
	@Test
	void testGetAllBooksOKStatus() {
		when(bookRepository.findAll()).thenReturn(listBook);
		ResponseEntity<List<Book>> responseEntity = bookService.getAllBooks();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(!responseEntity.getBody().isEmpty());
	}

	@DisplayName("Test get all Books - NOT FOUND")
	@Test
	void testGetAllBooksNotFoundStatus() {
		when(bookRepository.findAll()).thenReturn(null);
		ResponseEntity<List<Book>> responseEntity = bookService.getAllBooks();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		when(bookRepository.findAll()).thenReturn(new LinkedList<>());
		responseEntity = bookService.getAllBooks();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test get Book by ID - OK")
	@Test
	void testGetBookByIdOKStatus() {
		when(bookRepository.findById(any(Integer.class))).thenReturn(Optional.of(book));
		ResponseEntity<Book> responseEntity = bookService.getBookById(1);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@DisplayName("Test get Book by ID - Not Found")
	@Test
	void testGetBookByIdNotFoundStatus() {
		when(bookRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));
		ResponseEntity<Book> responseEntity = bookService.getBookById(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test get Book by Name - OK")
	@Test
	void testGetBookByNameOKStatus() {
		when(bookRepository.findByBookNameContaining(any(String.class))).thenReturn(listBook);
		ResponseEntity<List<Book>> responseEntity = bookService.getBookByName("name1");
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@DisplayName("Test get Book by Name - Not Found")
	@Test
	void testGetBookByNameNotFoundStatus() {
		when(bookRepository.findByBookNameContaining(any(String.class))).thenReturn(null);
		ResponseEntity<List<Book>> responseEntity = bookService.getBookByName("name1");
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		when(bookRepository.findByBookNameContaining(any(String.class))).thenReturn(new LinkedList<>());
		responseEntity = bookService.getBookByName("name1");
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test issue book - OK")
	@Test
	void testIssueBookByIdOKStatus() {
		when(bookRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(book));
		when(bookRepository.save(any(Book.class))).thenReturn(book);
		ResponseEntity<String> responseEntity = bookService.issueBookById(1);
		assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book Issued", responseEntity.getBody());
	}

	@DisplayName("Test issue book - Not Found")
	@Test
	void testIssueBookByIdNotFoundStatus() {
		when(bookRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));
		ResponseEntity<String> responseEntity = bookService.issueBookById(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book not found", responseEntity.getBody());
	}

	@DisplayName("Test issue book - Unprocessable Entity")
	@Test
	void testIssueBookByIdUnprocessableEntityStatus() {
		book.setAvailableCopies(0);
		when(bookRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(book));
		ResponseEntity<String> responseEntity = bookService.issueBookById(1);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("All copies are already issued", responseEntity.getBody());
	}

	@DisplayName("Test return book - OK")
	@Test
	void testReturnBookByIdOKStatus() {
		when(bookRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(book));
		when(bookRepository.save(any(Book.class))).thenReturn(book);
		ResponseEntity<String> responseEntity = bookService.returnBookById(1);
		assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book Returned", responseEntity.getBody());
	}

	@DisplayName("Test return book - Not Found")
	@Test
	void testReturnBookByIdNotFoundStatus() {
		when(bookRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));
		ResponseEntity<String> responseEntity = bookService.returnBookById(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book not found", responseEntity.getBody());
	}

	@DisplayName("Test return book - Unprocessable Entity")
	@Test
	void testReturnBookByIdUnprocessableEntityStatus() {
		book.setAvailableCopies(7);
		when(bookRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(book));
		ResponseEntity<String> responseEntity = bookService.returnBookById(1);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Invalid return. All copies of given ID are already returned.", responseEntity.getBody());
	}

	@DisplayName("Test save book - OK")
	@Test
	void testSaveBookOKStatus() {
		when(bookRepository.existsById(any(Integer.class))).thenReturn(false);
		when(bookRepository.save(any(Book.class))).thenReturn(book);
		ResponseEntity<String> responseEntity = bookService.saveBook(book);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book saved", responseEntity.getBody());
	}

	@DisplayName("Test save book - Unprocessable Entity")
	@Test
	void testSaveBookUnprocessableEntityStatus() {
		when(bookRepository.existsById(any(Integer.class))).thenReturn(true);
		ResponseEntity<String> responseEntity = bookService.saveBook(book);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book already present", responseEntity.getBody());
	}

	@DisplayName("Test save book - null book")
	@Test
	void testSaveBookBadRequestStatus() {
		ResponseEntity<String> responseEntity = bookService.saveBook(null);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book not provided", responseEntity.getBody());
	}

	@DisplayName("Test remove book - OK")
	@Test
	void testRemoveBookOKStatus() {
		when(bookRepository.existsById(any(Integer.class))).thenReturn(true);
		ResponseEntity<String> responseEntity = bookService.removeBook(1);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book deleted", responseEntity.getBody());
	}

	@DisplayName("Test remove book - Not Found")
	@Test
	void testRemoveBookUnprocessableEntityStatus() {
		when(bookRepository.existsById(any(Integer.class))).thenReturn(false);
		ResponseEntity<String> responseEntity = bookService.removeBook(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book not found", responseEntity.getBody());
	}

	@DisplayName("Test remove book - null book")
	@Test
	void testRemoveBookBadRequestStatus() {
		ResponseEntity<String> responseEntity = bookService.removeBook(null);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book ID cannot be empty", responseEntity.getBody());
	}

}
