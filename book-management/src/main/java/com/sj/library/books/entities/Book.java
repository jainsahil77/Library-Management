package com.sj.library.books.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Book entity
 * 
 * @author Sahil Jain
 *
 */
@Entity
@Data
public class Book {
	@Id
	@Column(name = "book_id", nullable = false)
	@NotNull(message = "Book ID is mandatory")
	private int bookId;
	@Column(name = "book_name", nullable = false)
	@NotBlank(message = "Book name is mandatory")
	private String bookName;
	@Column(name = "author_name")
	private String authorName;
	@Column(name = "total_copies", nullable = false)
	@NotNull(message = "Total copies is mandatory")
	private int totalCopies;
	@Column(name = "available_copies", nullable = false)
	@NotNull(message = "Available copies is mandatory")
	private int availableCopies;
}
