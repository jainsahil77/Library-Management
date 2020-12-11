package com.sj.library.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot application for managing books in a library. It has APIs for
 * adding, removing, issuing and returning book along with various get APIs
 * 
 * @author Sahil Jain
 *
 */
@SpringBootApplication
public class BookManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookManagementApplication.class, args);
	}

}
