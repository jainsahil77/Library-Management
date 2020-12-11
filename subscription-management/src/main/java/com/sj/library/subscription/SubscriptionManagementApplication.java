package com.sj.library.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A spring boot application for managing subscription of books. Service
 * interacts with book service for issuing and returning books
 * 
 * @author Sahil Jain
 *
 */
@SpringBootApplication
public class SubscriptionManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionManagementApplication.class, args);
	}

}
