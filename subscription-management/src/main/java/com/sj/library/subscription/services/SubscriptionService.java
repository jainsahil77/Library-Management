package com.sj.library.subscription.services;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sj.library.subscription.client.BookClient;
import com.sj.library.subscription.entities.Member;
import com.sj.library.subscription.entities.Subscription;
import com.sj.library.subscription.repositories.MemberRepository;
import com.sj.library.subscription.repositories.SubscriptionRepository;

/**
 * Service class for managing subscription. It communicates with Book service
 * for book issue and return.
 * 
 * @author Sahil.Jain
 *
 */

@Service
public class SubscriptionService {
	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	BookClient bookClient;

	/**
	 * Create new subscription
	 * 
	 * @param subscription - Subscription entity
	 * @return ResponseEntity with description and status
	 */
	@Transactional
	public ResponseEntity<String> addSubscription(Subscription subscription) {
		if (Objects.nonNull(subscription)) {
			if (subscriptionRepository.existsById(subscription.getSubscriptionId()))
				return new ResponseEntity<>("Subscription already registered", HttpStatus.UNPROCESSABLE_ENTITY);

			int bookId = subscription.getBookId();
			if (bookId == 0)
				return new ResponseEntity<>("Book ID is mandatory", HttpStatus.BAD_REQUEST);

			int memberId = subscription.getMemberId();
			// Check if member id is provided or not
			if (memberId == 0)
				return new ResponseEntity<>("Member ID is mandatory", HttpStatus.BAD_REQUEST);

			// Check if member exists or not
			Optional<Member> memberOptional = memberRepository.findById(memberId);
			if (memberOptional.isPresent()) {
				Member member = memberOptional.get();
				// Check member's membership status
				if (member.isMembershipStatus())
					subscription.setMember(member);
				else
					return new ResponseEntity<>("Member's membership is not valid", HttpStatus.UNPROCESSABLE_ENTITY);
			} else
				return new ResponseEntity<>("Member is not registered", HttpStatus.UNPROCESSABLE_ENTITY);

			Optional<String> bookServiceResponse = bookClient.getWebClient().patch().uri("/issue/" + bookId).accept(MediaType.APPLICATION_JSON)
					.exchangeToMono(response -> response.bodyToMono(String.class)).blockOptional(Duration.ofSeconds(5));

			if (bookServiceResponse.isPresent()) {
				String responseStr = bookServiceResponse.get();
				if (responseStr.equalsIgnoreCase("Book Issued")) {
					// If subscription date is not provided, set current system date
					if (Objects.isNull(subscription.getSubscriptionDate()))
						subscription.setSubscriptionDate(new Date());
					// Unset return date as book is beign issues, not returned
					subscription.setReturnDate(null);

					// Save subscription
					subscriptionRepository.save(subscription);
					return new ResponseEntity<>("Subscription Registered", HttpStatus.CREATED);
				} else
					return new ResponseEntity<>("Subscription failed : " + responseStr, HttpStatus.UNPROCESSABLE_ENTITY);
			} else
				return new ResponseEntity<>("Subscription failed : Book service response is null", HttpStatus.INTERNAL_SERVER_ERROR);
		} else
			return new ResponseEntity<>("Subscription not provided", HttpStatus.BAD_REQUEST);
	}

	/**
	 * End subscription by subscription ID
	 * 
	 * @param subscriptionId - Subscription ID
	 * @return ResponseEntity with description and status
	 */
	public ResponseEntity<String> endSubscription(Integer subscriptionId) {
		Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(subscriptionId);
		if (subscriptionOptional.isPresent()) {
			Subscription subscription = subscriptionOptional.get();
			if (subscription.isBookReturned() && Objects.nonNull(subscription.getReturnDate()))
				return new ResponseEntity<>("Subscription already ended on " + subscription.getReturnDate(), HttpStatus.UNPROCESSABLE_ENTITY);

			Optional<String> bookServiceResponse = bookClient.getWebClient().patch().uri("/return/" + subscription.getBookId()).accept(MediaType.APPLICATION_JSON)
					.exchangeToMono(response -> response.bodyToMono(String.class)).blockOptional(Duration.ofSeconds(5));

			if (bookServiceResponse.isPresent()) {
				String responseStr = bookServiceResponse.get();
				if (responseStr.equalsIgnoreCase("Book Returned")) {
					subscription.setBookReturned(true);
					subscription.setReturnDate(new Date());
					subscriptionRepository.save(subscription);
					return new ResponseEntity<>("Subscription ended successfully", HttpStatus.OK);
				} else
					return new ResponseEntity<>("Subscription failed to end : " + responseStr, HttpStatus.UNPROCESSABLE_ENTITY);
			} else
				return new ResponseEntity<>("Subscription failed to end : Book service response is null", HttpStatus.INTERNAL_SERVER_ERROR);

		} else
			return new ResponseEntity<>("Subscription ID is not valid", HttpStatus.NOT_FOUND);
	}

	/**
	 * Get all subscription present in DB
	 * 
	 * @return ResponseEntity with list of all subscription
	 */
	public ResponseEntity<List<Subscription>> getAllSubscriptions() {
		List<Subscription> allSubscriptions = subscriptionRepository.findAll();
		if (Objects.nonNull(allSubscriptions) && !allSubscriptions.isEmpty()) {
			allSubscriptions.stream().forEach(subscription -> subscription.setMemberId(subscription.getMember().getMemberId()));
			return new ResponseEntity<>(allSubscriptions, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get subscription by ID
	 * 
	 * @param id - Subscription ID
	 * @return ResponseEntity with subscription entity
	 */
	public ResponseEntity<Subscription> getSubscriptionById(Integer id) {
		Optional<Subscription> optional = subscriptionRepository.findById(id);
		if (optional.isPresent()) {
			Subscription subscription = optional.get();
			subscription.setMemberId(subscription.getMember().getMemberId());
			return new ResponseEntity<>(subscription, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get all subscriptions associated with given member ID
	 * 
	 * @param id - Member ID
	 * @return ResponseEntity with list of subscriptions
	 */
	public ResponseEntity<List<Subscription>> getSubscriptionByMemberId(Integer id) {
		List<Subscription> subscriptionList = subscriptionRepository.findByMemberId(id);
		if (Objects.nonNull(subscriptionList) && !subscriptionList.isEmpty()) {
			subscriptionList.stream().forEach(subscription -> subscription.setMemberId(subscription.getMember().getMemberId()));
			return new ResponseEntity<>(subscriptionList, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get all subscription based on their current status
	 * 
	 * @param returnStatus - Subscription status
	 * @return ResponseEntity with list subscription
	 */
	public ResponseEntity<List<Subscription>> getSubscriptionByReturnStatus(boolean returnStatus) {
		List<Subscription> subscriptionList = subscriptionRepository.findByBookReturned(returnStatus);
		if (Objects.nonNull(subscriptionList) && !subscriptionList.isEmpty()) {
			subscriptionList.stream().forEach(subscription -> subscription.setMemberId(subscription.getMember().getMemberId()));
			return new ResponseEntity<>(subscriptionList, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
