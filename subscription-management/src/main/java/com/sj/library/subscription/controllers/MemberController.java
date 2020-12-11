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

import com.sj.library.subscription.entities.Member;
import com.sj.library.subscription.services.MemberService;

/**
 * Rest controller for exposing member APIs for adding new member, terminating
 * membership, enabling membership and various get methods
 * 
 * @author Sahil Jain
 *
 */
@RestController
@RequestMapping(value = "/member-management")
public class MemberController {
	@Autowired
	private MemberService memberService;

	/**
	 * Get list of all members
	 * 
	 * @return ResponseEntity with list of members
	 */
	@GetMapping(value = "/members")
	public ResponseEntity<List<Member>> getAllMembers() {
		return memberService.getAllMembers();
	}

	/**
	 * Get member by member ID
	 * 
	 * @param id - Member ID
	 * @return ResponseEntity with member entity
	 */
	@GetMapping(value = "/memberByID/{id}")
	public ResponseEntity<Member> getMemberById(@PathVariable(name = "id", required = true) Integer id) {
		return memberService.getMemberById(id);
	}

	/**
	 * Get list of all member containing given member name
	 * 
	 * @param name - Member name
	 * @return ResponseEntity with list of members
	 */
	@GetMapping(value = "/memberByName/{name}")
	public ResponseEntity<List<Member>> getMemberByName(@PathVariable(name = "name", required = true) String name) {
		return memberService.getMemberByName(name);
	}

	/**
	 * Add new member
	 * 
	 * @param member - Member entity
	 * @return ResponseEntity with description
	 */
	@PutMapping(value = "/add")
	public ResponseEntity<String> addNewMember(@Valid @RequestBody Member member) {
		return memberService.addMember(member);
	}

	/**
	 * Terminate membership by setting membership status false
	 * 
	 * @param id - Member ID
	 * @return ResponseEntity with description
	 */
	@PatchMapping(value = "/terminate/{id}")
	public ResponseEntity<String> terminateMembership(@PathVariable(name = "id", required = true) Integer id) {
		return memberService.terminateMembership(id);
	}

	/**
	 * Enable member's membership status by setting it as true
	 * 
	 * @param id - Member ID
	 * @return ResponseEntity with description
	 */
	@PatchMapping(value = "/reissue/{id}")
	public ResponseEntity<String> reissueMembership(@PathVariable(name = "id", required = true) Integer id) {
		return memberService.enableMembership(id);
	}
}
