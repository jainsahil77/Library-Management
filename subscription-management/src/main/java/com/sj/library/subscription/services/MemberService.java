package com.sj.library.subscription.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sj.library.subscription.entities.Member;
import com.sj.library.subscription.repositories.MemberRepository;

/**
 * 
 * Service class for adding, getting and terminating members
 * 
 * @author Sahil Jain
 */
@Service
@Transactional
public class MemberService {
	@Autowired
	MemberRepository memberRepository;

	/**
	 * Add new member if not present in DB
	 * 
	 * @param member - Member entity
	 * @return ResponseEntity with description message and status
	 */
	public ResponseEntity<String> addMember(Member member) {
		if (Objects.nonNull(member)) {
			if (memberRepository.existsById(member.getMemberId()))
				return new ResponseEntity<>("Member already registered", HttpStatus.UNPROCESSABLE_ENTITY);
			memberRepository.save(member);
			return new ResponseEntity<>("Member Registered", HttpStatus.CREATED);
		} else
			return new ResponseEntity<>("Member not provided", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Terminates membership by setting membershipStatus false in DB
	 * 
	 * @param id - Member ID
	 * @return ResponseEntity with description message and status
	 */
	public ResponseEntity<String> terminateMembership(Integer id) {
		if (Objects.nonNull(id)) {
			Optional<Member> memberOptional = memberRepository.findById(id);
			if (!memberOptional.isPresent())
				return new ResponseEntity<>("Member not found", HttpStatus.NOT_FOUND);
			Member member = memberOptional.get();
			if (!member.isMembershipStatus())
				return new ResponseEntity<>("Membership already terminated", HttpStatus.UNPROCESSABLE_ENTITY);
			member.setMembershipStatus(false);
			memberRepository.save(member);
			return new ResponseEntity<>("Membership terminated", HttpStatus.OK);
		} else
			return new ResponseEntity<>("Member ID cannot be empty", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Enables membership by setting membership status to true in DB
	 * 
	 * @param id - Member ID
	 * @return ResponseEntity with description message and status
	 */
	public ResponseEntity<String> enableMembership(Integer id) {
		if (Objects.nonNull(id)) {
			Optional<Member> memberOptional = memberRepository.findById(id);
			if (!memberOptional.isPresent())
				return new ResponseEntity<>("Member not found", HttpStatus.NOT_FOUND);
			Member member = memberOptional.get();
			if (member.isMembershipStatus())
				return new ResponseEntity<>("Membership already reissued", HttpStatus.UNPROCESSABLE_ENTITY);
			member.setMembershipStatus(true);
			memberRepository.save(member);
			return new ResponseEntity<>("Membership reissued", HttpStatus.OK);
		} else
			return new ResponseEntity<>("Member ID cannot be empty", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Get all members
	 * 
	 * @return ResponseEntity with list of all members
	 */
	public ResponseEntity<List<Member>> getAllMembers() {
		List<Member> allMembers = memberRepository.findAll();
		if (Objects.nonNull(allMembers) && !allMembers.isEmpty())
			return new ResponseEntity<>(allMembers, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get member entity by ID
	 * 
	 * @param id - Member ID
	 * @return ResponseEntity with member entity
	 */
	public ResponseEntity<Member> getMemberById(Integer id) {
		Optional<Member> optional = memberRepository.findById(id);
		if (optional.isPresent())
			return new ResponseEntity<>(optional.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get list of member entity by name.
	 * 
	 * @param memberName - Member's name
	 * @return ResponseEntity with List of members
	 */
	public ResponseEntity<List<Member>> getMemberByName(String memberName) {
		List<Member> memberList = memberRepository.findByMemberNameContaining(memberName);
		if (Objects.nonNull(memberList) && !memberList.isEmpty())
			return new ResponseEntity<>(memberList, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get list of members on the basis of their membership status
	 * 
	 * @param status - Membership status
	 * @return ResponseEntity with list of members
	 */
	public ResponseEntity<List<Member>> getMemberByStatus(boolean status) {
		List<Member> memberList = memberRepository.findByMembershipStatus(status);
		if (Objects.nonNull(memberList) && !memberList.isEmpty())
			return new ResponseEntity<>(memberList, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
