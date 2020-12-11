package com.sj.library.books.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sj.library.books.entities.Book;
import com.sj.library.books.services.BookService;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
	@InjectMocks
	private BookController bookcontroller;

	@Mock
	private BookService bookService;

	@Autowired
	MockMvc mockMvc;

	private Book book;
	private List<Book> listBook;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(bookcontroller).build();
		book = new Book();
		book.setAuthorName("author");
		book.setAvailableCopies(5);
		book.setBookId(1);
		book.setBookName("book");
		book.setTotalCopies(7);
		listBook = new LinkedList<>();
		listBook.add(book);
	}

	@DisplayName("Test get all Books")
	@Test
	void testGetAllBooks() throws Exception {
		RequestBuilder requestBuilder = get("/books-management/books").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(bookService, Mockito.times(1)).getAllBooks();
	}

	@DisplayName("Test get book by ID")
	@Test
	void testGetBookById() throws Exception {
		when(bookService.getBookById(any(Integer.class))).thenReturn(new ResponseEntity<>(book, HttpStatus.OK));
		RequestBuilder requestBuilder = get("/books-management/bookByID/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(bookService, Mockito.times(1)).getBookById(eq(1));
	}

	@DisplayName("Test get book by name")
	@Test
	void testGetBookByName() throws Exception {
		when(bookService.getBookByName(any(String.class))).thenReturn(new ResponseEntity<>(listBook, HttpStatus.OK));
		RequestBuilder requestBuilder = get("/books-management/bookByName/name1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(bookService, Mockito.times(1)).getBookByName(eq("name1"));
	}

	@DisplayName("Test issue book")
	@Test
	void testIssueBook() throws Exception {
		when(bookService.issueBookById(any(Integer.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		RequestBuilder requestBuilder = patch("/books-management/issue/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(bookService, Mockito.times(1)).issueBookById(eq(1));
	}

	@DisplayName("Test return book")
	@Test
	void testReturnBook() throws Exception {
		when(bookService.returnBookById(any(Integer.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		RequestBuilder requestBuilder = patch("/books-management/return/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(bookService, Mockito.times(1)).returnBookById(eq(1));
	}

	@DisplayName("Test add new book")
	@Test
	void testAddNewBook() throws Exception {
		when(bookService.saveBook(any(Book.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		RequestBuilder requestBuilder = put("/books-management/add").contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.content("{\"bookId\": 1,\"bookName\": \"book\",\"authorName\": \"author\",\"totalCopies\": 7,\"availableCopies\": 5}");
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(bookService, Mockito.times(1)).saveBook(eq(book));
	}

	@DisplayName("Test remove book")
	@Test
	void testRemoveBook() throws Exception {
		when(bookService.removeBook(any(Integer.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		RequestBuilder requestBuilder = delete("/books-management/remove/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(bookService, Mockito.times(1)).removeBook(eq(1));
	}
}
