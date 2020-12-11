package com.sj.library.subscription.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sj.library.subscription.entities.Subscription;
import com.sj.library.subscription.services.SubscriptionService;

/**
 * Rest controller for exposing APIs for managing subscriptions along wiht get
 * APIs
 * 
 * @author Sahil Jain
 *
 */
@RestController
@RequestMapping(value = "/subscription-management")
public class SubscriptionController {
	@Autowired
	private SubscriptionService subscriptionService;

	/**
	 * Get all subscriptions
	 * 
	 * @return ResponseEntity with list of subscription
	 */
	@GetMapping(value = "/subscriptions")
	public ResponseEntity<List<Subscription>> getAllSubscriptions() {
		return subscriptionService.getAllSubscriptions();
	}

	/**
	 * Get subscription by subscription ID
	 * 
	 * @param id - Subscription ID
	 * @return ResponseEntity with subscription entity
	 */
	@GetMapping(value = "/subscriptionByID/{id}")
	public ResponseEntity<Subscription> getSubscriptionById(@PathVariable(name = "id", required = true) Integer id) {
		return subscriptionService.getSubscriptionById(id);
	}

	/**
	 * Get list of subscription assciated with provided member ID
	 * 
	 * @param id - Member ID
	 * @return ResponseEntity with list of subscription
	 */
	@GetMapping(value = "/subscriptionByMemberID/{id}")
	public ResponseEntity<List<Subscription>> getSubscriptionByMemberId(@PathVariable(name = "id", required = true) Integer id) {
		return subscriptionService.getSubscriptionByMemberId(id);
	}

	/**
	 * Get list of all subscriptions which has subscription status false
	 * 
	 * @return ResponseEntity with list of subscription
	 */
	@GetMapping(value = "/closedSubscriptions")
	public ResponseEntity<List<Subscription>> getClosedSubscription() {
		return subscriptionService.getSubscriptionByReturnStatus(true);
	}

	/**
	 * Get list of subscription which has subscription status true
	 * 
	 * @return ResponseEntity with list of subscription
	 */
	@GetMapping(value = "/openSubscriptions")
	public ResponseEntity<List<Subscription>> getOpenSubscription() {
		return subscriptionService.getSubscriptionByReturnStatus(false);
	}

	/**
	 * Add new subscription
	 * 
	 * @param subscription - Subscription entity
	 * @return ResponseEntity with description
	 */
	@PutMapping(value = "/subscribe")
	public ResponseEntity<String> addNewSubscription(@Valid @RequestBody Subscription subscription) {
		return subscriptionService.addSubscription(subscription);
	}

	/**
	 * End subscription
	 * 
	 * @param id - Subscription ID
	 * @return ResponseEntity with description
	 */
	@PatchMapping(value = "/endSubscription/{id}")
	public ResponseEntity<String> endSubscription(@PathVariable(name = "id", required = true) Integer id) {
		return subscriptionService.endSubscription(id);
	}

}
