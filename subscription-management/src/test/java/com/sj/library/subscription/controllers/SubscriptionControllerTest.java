package com.sj.library.subscription.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Date;
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

import com.sj.library.subscription.entities.Subscription;
import com.sj.library.subscription.services.SubscriptionService;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {
	@InjectMocks
	private SubscriptionController subscriptionController;

	@Mock
	private SubscriptionService subscriptionService;

	@Autowired
	MockMvc mockMvc;

	private Subscription subscription;
	private List<Subscription> listSubscription;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(subscriptionController).build();
		subscription = new Subscription();
		subscription.setBookId(1);
		subscription.setBookReturned(true);
		subscription.setMember(null);
		subscription.setMemberId(1);
		subscription.setReturnDate(new Date());
		subscription.setSubscriptionId(1);
		listSubscription = new LinkedList<>();
		listSubscription.add(subscription);
	}

	@DisplayName("Test get all subscriptions")
	@Test
	void testGetAllSubscriptions() throws Exception {
		RequestBuilder requestBuilder = get("/subscription-management/subscriptions").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(subscriptionService, Mockito.times(1)).getAllSubscriptions();
	}

	@DisplayName("Test get subscription by ID")
	@Test
	void testGetSubscriptionById() throws Exception {
		RequestBuilder requestBuilder = get("/subscription-management/subscriptionByID/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(subscriptionService, Mockito.times(1)).getSubscriptionById(any(Integer.class));
	}

	@DisplayName("Test get subscription by member ID")
	@Test
	void testGetSubscriptionByMemberId() throws Exception {
		RequestBuilder requestBuilder = get("/subscription-management/subscriptionByMemberID/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(subscriptionService, Mockito.times(1)).getSubscriptionByMemberId(any(Integer.class));
	}

	@DisplayName("Test get closed subscription")
	@Test
	void testGetClosedSubscription() throws Exception {
		RequestBuilder requestBuilder = get("/subscription-management/closedSubscriptions").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(subscriptionService, Mockito.times(1)).getSubscriptionByReturnStatus(any(Boolean.class));
	}

	@DisplayName("Test get open subscription")
	@Test
	void testGetOpenSubscription() throws Exception {
		RequestBuilder requestBuilder = get("/subscription-management/openSubscriptions").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(subscriptionService, Mockito.times(1)).getSubscriptionByReturnStatus(any(Boolean.class));
	}

	@DisplayName("Test end Subscription")
	@Test
	void testEndSubscription() throws Exception {
		RequestBuilder requestBuilder = patch("/subscription-management/endSubscription/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(subscriptionService, Mockito.times(1)).endSubscription(any(Integer.class));
	}

	@DisplayName("Test add new subscription")
	@Test
	void testAddNewSubscription() throws Exception {
		when(subscriptionService.addSubscription(any(Subscription.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		RequestBuilder requestBuilder = put("/subscription-management/subscribe").contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.content("{\"subscriptionId\": 4,\"subscriptionDate\": \"2020-12-02T18:30:00.000+00:00\",\"returnDate\": null,\"bookReturned\": false,\"bookId\": 1,\"memberId\": 1}");
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(subscriptionService, Mockito.times(1)).addSubscription(any(Subscription.class));
	}

}
