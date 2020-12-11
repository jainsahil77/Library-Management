package com.sj.library.subscription.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

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

import com.sj.library.subscription.entities.Member;
import com.sj.library.subscription.repositories.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	private Member member;
	private List<Member> listMember;

	@BeforeEach
	public void setup() {
		member = new Member();
		listMember = new LinkedList<>();
		listMember.add(member);
	}

	@DisplayName("Test get all Members - OK")
	@Test
	void testGetAllMembersOKStatus() {
		when(memberRepository.findAll()).thenReturn(listMember);
		ResponseEntity<List<Member>> responseEntity = memberService.getAllMembers();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(!responseEntity.getBody().isEmpty());
	}

	@DisplayName("Test get all Members - Not Found")
	@Test
	void testGetAllMembersNotFoundStatus() {
		when(memberRepository.findAll()).thenReturn(new LinkedList<>());
		ResponseEntity<List<Member>> responseEntity = memberService.getAllMembers();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		when(memberRepository.findAll()).thenReturn(null);
		responseEntity = memberService.getAllMembers();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test get Member by ID - OK")
	@Test
	void testGetMemberByIdOKStatus() {
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(member));
		ResponseEntity<Member> responseEntity = memberService.getMemberById(1);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@DisplayName("Test get Member by ID - Not Found")
	@Test
	void testGetMemberByIdNotFoundStatus() {
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));
		ResponseEntity<Member> responseEntity = memberService.getMemberById(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test get Member by Name - OK")
	@Test
	void testGetMemberByNameOKStatus() {
		when(memberRepository.findByMemberNameContaining(any(String.class))).thenReturn(listMember);
		ResponseEntity<List<Member>> responseEntity = memberService.getMemberByName("name");
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertTrue(!responseEntity.getBody().isEmpty());
	}

	@DisplayName("Test get Member by Name - Not Found")
	@Test
	void testGetMemberByNameNotFoundStatus() {
		when(memberRepository.findByMemberNameContaining(any(String.class))).thenReturn(null);
		ResponseEntity<List<Member>> responseEntity = memberService.getMemberByName("name");
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		when(memberRepository.findByMemberNameContaining(any(String.class))).thenReturn(new LinkedList<>());
		responseEntity = memberService.getMemberByName("name");
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test get Member by Status - OK")
	@Test
	void testGetMemberByStatusOKStatus() {
		when(memberRepository.findByMembershipStatus(any(Boolean.class))).thenReturn(listMember);
		ResponseEntity<List<Member>> responseEntity = memberService.getMemberByStatus(true);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertTrue(!responseEntity.getBody().isEmpty());
	}

	@DisplayName("Test get Member by Status - Not Found")
	@Test
	void testGetMemberByStatusNotFoundStatus() {
		when(memberRepository.findByMembershipStatus(any(Boolean.class))).thenReturn(null);
		ResponseEntity<List<Member>> responseEntity = memberService.getMemberByStatus(true);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		when(memberRepository.findByMembershipStatus(any(Boolean.class))).thenReturn(new LinkedList<>());
		responseEntity = memberService.getMemberByStatus(true);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@DisplayName("Test Add member - OK")
	@Test
	void testAddMemberOKStatus() {
		when(memberRepository.save(any(Member.class))).thenReturn(member);
		when(memberRepository.existsById(any(Integer.class))).thenReturn(false);
		ResponseEntity<String> responseEntity = memberService.addMember(member);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member Registered", responseEntity.getBody());
	}

	@DisplayName("Test Add member - Unprocessable Entity")
	@Test
	void testAddMemberUnprocessableEntityStatus() {
		when(memberRepository.existsById(any(Integer.class))).thenReturn(true);
		ResponseEntity<String> responseEntity = memberService.addMember(member);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member already registered", responseEntity.getBody());
	}

	@DisplayName("Test Add member - Bad Request")
	@Test
	void testAddMemberBadRequestStatus() {
		ResponseEntity<String> responseEntity = memberService.addMember(null);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member not provided", responseEntity.getBody());
	}

	@DisplayName("Test terminate membership - OK")
	@Test
	void testTerminateMembershipOKStatus() {
		member.setMembershipStatus(true);
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(member));
		when(memberRepository.save(any(Member.class))).thenReturn(member);
		ResponseEntity<String> responseEntity = memberService.terminateMembership(1);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Membership terminated", responseEntity.getBody());
	}

	@DisplayName("Test terminate membership - Not Found")
	@Test
	void testTerminateMembershipNotFoundStatus() {
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));
		ResponseEntity<String> responseEntity = memberService.terminateMembership(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member not found", responseEntity.getBody());
	}

	@DisplayName("Test terminate membership - Unprocessable Entity")
	@Test
	void testTerminateMembershipUnprocessableEntityStatus() {
		member.setMembershipStatus(false);
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(member));
		ResponseEntity<String> responseEntity = memberService.terminateMembership(1);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Membership already terminated", responseEntity.getBody());
	}

	@DisplayName("Test terminate membership - Bad Request")
	@Test
	void testTerminateMembershipBadRequestStatus() {
		ResponseEntity<String> responseEntity = memberService.terminateMembership(null);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member ID cannot be empty", responseEntity.getBody());
	}

	@DisplayName("Test enable membership - OK")
	@Test
	void testEnableMembershipOKStatus() {
		member.setMembershipStatus(false);
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(member));
		when(memberRepository.save(any(Member.class))).thenReturn(member);
		ResponseEntity<String> responseEntity = memberService.enableMembership(1);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Membership reissued", responseEntity.getBody());
	}

	@DisplayName("Test enable membership - Not Found")
	@Test
	void testEnableMembershipNotFoundStatus() {
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(null));
		ResponseEntity<String> responseEntity = memberService.enableMembership(1);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member not found", responseEntity.getBody());
	}

	@DisplayName("Test enable membership - Unprocessable Entity")
	@Test
	void testEnableMembershipUnprocessableEntityStatus() {
		member.setMembershipStatus(true);
		when(memberRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(member));
		ResponseEntity<String> responseEntity = memberService.enableMembership(1);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Membership already reissued", responseEntity.getBody());
	}

	@DisplayName("Test enable membership - Bad Request")
	@Test
	void testEnableMembershipBadRequestStatus() {
		ResponseEntity<String> responseEntity = memberService.enableMembership(null);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Member ID cannot be empty", responseEntity.getBody());
	}

}
