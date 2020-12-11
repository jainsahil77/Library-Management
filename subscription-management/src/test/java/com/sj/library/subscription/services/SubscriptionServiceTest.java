package com.sj.library.subscription.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;
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
import org.springframework.web.reactive.function.client.WebClient;

import com.sj.library.subscription.client.BookClient;
import com.sj.library.subscription.entities.Member;
import com.sj.library.subscription.entities.Subscription;
import com.sj.library.subscription.repositories.MemberRepository;
import com.sj.library.subscription.repositories.SubscriptionRepository;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

	@InjectMocks
	private SubscriptionService subscriptionService;

	@Mock
	private SubscriptionRepository subscriptionRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	BookClient bookClient;

	private Subscription subscription;
	private Member member;
	private List<Subscription> listSubscription;

	@BeforeEach
	public void setup() {
		subscription = new Subscription();
		member = new Member();
		member.setMemberId(1);
		member.setMembershipStatus(true);
		subscription.setBookId(1);
		subscription.setMemberId(1);
		subscription.setMember(member);
		listSubscription = new LinkedList<>();
		listSubscription.add(subscription);
	}

	@DisplayName("Test get all Subscriptions - OK")
	@Test
	void testGetAllSubscriptionsOKStatus() {
		when(subscriptionRepository.findAll()).thenReturn(listSubscription);
		ResponseEntity<List<Subscription>> responseEntity = subscriptionService.getAllSubscriptions();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(!responseEntity.getBody().isEmpty());
	}

	@DisplayName("Test get all Subscriptions - Not Found")
	@Test
	void testGetAllSubscriptionsNotFoundStatus() {
		when(subscriptionRepository.findAll()).thenReturn(new LinkedList<>());
		ResponseEntity<List<Subscription>> responseEntity = subscriptionService.getAllSubscriptions();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		when(subscriptionRepository.findAll()).thenReturn(null);
		responseEntity = subscriptionService.getAllSubscriptions();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test get Subscription by ID - OK")
	@Test
	void testGetSubscriptionByIdOKStatus() {
		when(subscriptionRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(subscription));
		ResponseEntity<Subscription> responseEntity = subscriptionService.getSubscriptionById(1);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@DisplayName("Test get Subscription by ID - Not Found")
	@Test
	void testGetSubscriptionByIdNotFoundStatus() {
		when(subscriptionRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));
		ResponseEntity<Subscription> responseEntity = subscriptionService.getSubscriptionById(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test get Subscription by Member ID - OK")
	@Test
	void testGetSubscriptionByMemberIdOKStatus() {
		when(subscriptionRepository.findByMemberId(any(Integer.class))).thenReturn(listSubscription);
		ResponseEntity<List<Subscription>> responseEntity = subscriptionService.getSubscriptionByMemberId(1);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@DisplayName("Test get Subscription by Member ID - Not Found")
	@Test
	void testGetSubscriptionByMemberIdNotFoundStatus() {
		when(subscriptionRepository.findByMemberId(any(Integer.class))).thenReturn(null);
		ResponseEntity<List<Subscription>> responseEntity = subscriptionService.getSubscriptionByMemberId(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		when(subscriptionRepository.findByMemberId(any(Integer.class))).thenReturn(new LinkedList<>());
		responseEntity = subscriptionService.getSubscriptionByMemberId(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test get Subscription by Return status - OK")
	@Test
	void testGetSubscriptionByReturnStatusOKStatus() {
		when(subscriptionRepository.findByBookReturned(any(Boolean.class))).thenReturn(listSubscription);
		ResponseEntity<List<Subscription>> responseEntity = subscriptionService.getSubscriptionByReturnStatus(true);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@DisplayName("Test get Subscription by Return status - Not Found")
	@Test
	void testGetSubscriptionByReturnStatusNotFoundStatus() {
		when(subscriptionRepository.findByBookReturned(any(Boolean.class))).thenReturn(null);
		ResponseEntity<List<Subscription>> responseEntity = subscriptionService.getSubscriptionByReturnStatus(true);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		when(subscriptionRepository.findByBookReturned(any(Boolean.class))).thenReturn(new LinkedList<>());
		responseEntity = subscriptionService.getSubscriptionByReturnStatus(true);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test add subscription - Unprocessable Entity")
	@Test
	void testAddSubscriptionUnprocessableEntityStatus() {
		when(subscriptionRepository.existsById(any(Integer.class))).thenReturn(true);
		ResponseEntity<String> responseEntity = subscriptionService.addSubscription(subscription);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Subscription already registered", responseEntity.getBody());

		when(subscriptionRepository.existsById(any(Integer.class))).thenReturn(false);
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));
		responseEntity = subscriptionService.addSubscription(subscription);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member is not registered", responseEntity.getBody());

		member.setMembershipStatus(false);
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(member));
		responseEntity = subscriptionService.addSubscription(subscription);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member's membership is not valid", responseEntity.getBody());
	}

	@DisplayName("Test add subscription - Bad Request")
	@Test
	void testAddSubscriptionBadRequestStatus() {
		ResponseEntity<String> responseEntity = subscriptionService.addSubscription(null);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Subscription not provided", responseEntity.getBody());

		subscription.setBookId(0);
		when(subscriptionRepository.existsById(any(Integer.class))).thenReturn(false);
		responseEntity = subscriptionService.addSubscription(subscription);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Book ID is mandatory", responseEntity.getBody());

		subscription.setBookId(1);
		subscription.setMemberId(0);
		member.setMembershipStatus(false);
		responseEntity = subscriptionService.addSubscription(subscription);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member ID is mandatory", responseEntity.getBody());
	}

	@DisplayName("Test add subscription - Internal Server Error")
	@Test
	void testAddSubscriptionInternalServerErrorStatus() {
		when(subscriptionRepository.existsById(any(Integer.class))).thenReturn(false);
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(member));
		when(bookClient.getWebClient()).thenReturn(WebClient.builder().build());
		ResponseEntity<String> responseEntity = subscriptionService.addSubscription(subscription);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Subscription failed : Book service response is null", responseEntity.getBody());
	}

	@DisplayName("Test end subscription - Internal Server Error")
	@Test
	void testEndSubscriptionInternalServerErrorStatus() {
		subscription.setBookReturned(false);
		when(subscriptionRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(subscription));
		when(bookClient.getWebClient()).thenReturn(WebClient.builder().build());
		ResponseEntity<String> responseEntity = subscriptionService.endSubscription(1);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Subscription failed to end : Book service response is null", responseEntity.getBody());

		subscription.setBookReturned(true);
		when(subscriptionRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(subscription));
		when(bookClient.getWebClient()).thenReturn(WebClient.builder().build());
		responseEntity = subscriptionService.endSubscription(1);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Subscription failed to end : Book service response is null", responseEntity.getBody());
	}

	@DisplayName("Test end subscription - Unprocessable Entity")
	@Test
	void testEndSubscriptionUnprocessableEntityStatus() {
		subscription.setBookReturned(true);
		subscription.setReturnDate(new Date());
		when(subscriptionRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(subscription));
		ResponseEntity<String> responseEntity = subscriptionService.endSubscription(1);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Subscription already ended on " + subscription.getReturnDate(), responseEntity.getBody());
	}

	@DisplayName("Test end subscription - Not Found")
	@Test
	void testEndSubscriptionNotFoundStatus() {
		when(subscriptionRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));
		ResponseEntity<String> responseEntity = subscriptionService.endSubscription(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Subscription ID is not valid", responseEntity.getBody());
	}
}