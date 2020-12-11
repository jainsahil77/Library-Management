package com.sj.library.subscription.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sj.library.subscription.entities.Member;
import com.sj.library.subscription.services.MemberService;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {
	@InjectMocks
	private MemberController membercontroller;

	@Mock
	private MemberService memberservice;

	@Autowired
	MockMvc mockMvc;

	private Member member;
	private List<Member> listMember;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(membercontroller).build();
		member = new Member();
		member.setMemberId(1);
		member.setMemberName("name");
		member.setMembershipStartDate(new Date());
		member.setMembershipStatus(true);
		listMember = new LinkedList<>();
		listMember.add(member);
	}

	@DisplayName("Test get all members")
	@Test
	void testGetAllMembers() throws Exception {
		RequestBuilder requestBuilder = get("/member-management/members").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(memberservice, Mockito.times(1)).getAllMembers();
	}

	@DisplayName("Test get member by ID")
	@Test
	void testGetMemberById() throws Exception {
		RequestBuilder requestBuilder = get("/member-management/memberByID/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(memberservice, Mockito.times(1)).getMemberById(any(Integer.class));
	}

	@DisplayName("Test get member by Name")
	@Test
	void testGetMemberByName() throws Exception {
		RequestBuilder requestBuilder = get("/member-management/memberByName/name").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(memberservice, Mockito.times(1)).getMemberByName(any(String.class));
	}

	@DisplayName("Test add new member")
	@Test
	void testAddMember() throws Exception {
		RequestBuilder requestBuilder = put("/member-management/add").contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.content("{\"memberId\": 1,\"memberName\": \"name\",\"membershipStartDate\": \"2020-12-02T18:30:00.000+00:00\",\"membershipStatus\": true}");
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(memberservice, Mockito.times(1)).addMember(any(Member.class));
	}

	@DisplayName("Test terminate membership")
	@Test
	void testTerminateMembership() throws Exception {
		RequestBuilder requestBuilder = patch("/member-management/terminate/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(memberservice, Mockito.times(1)).terminateMembership(any(Integer.class));
	}

	@DisplayName("Test reissue membership")
	@Test
	void testReissueMembership() throws Exception {
		RequestBuilder requestBuilder = patch("/member-management/reissue/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		verify(memberservice, Mockito.times(1)).enableMembership(any(Integer.class));
	}

}
