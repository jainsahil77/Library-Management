package com.sj.library.subscription.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.sj.library.subscription.entities.Member;

/**
 * Repository for Member
 * 
 * @author Sahil Jain
 *
 */
@Repository
@RepositoryRestResource(exported = false)
public interface MemberRepository extends CrudRepository<Member, Integer> {
	/**
	 * Get list of all Member
	 */
	List<Member> findAll();

	/**
	 * Get Members based on their membership status
	 * 
	 * @param status - membership status
	 * @return List of Member
	 */
	List<Member> findByMembershipStatus(boolean status);

	/**
	 * Get list of Member containing given name
	 * 
	 * @param memberName - Member's name
	 * @return List of Member
	 */
	List<Member> findByMemberNameContaining(String memberName);
}
