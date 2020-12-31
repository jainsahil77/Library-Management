package com.sj.library.subscription.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sj.library.subscription.entities.Subscription;

/**
 * Repository for Subscription
 * 
 * @author Sahil Jain
 *
 */
@Repository
@RepositoryRestResource(exported = false)
public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
	/**
	 * Get list of all subscription
	 */
	List<Subscription> findAll();

	/**
	 * Get list of subscriptions associated with given member ID
	 * 
	 * @param id - Member ID
	 * @return List of subscription
	 */
	@Query(value = "select * from subscription where member_id = :id", nativeQuery = true)
	List<Subscription> findByMemberId(@Param("id") Integer id);

	/**
	 * Get list of subscription based on their status
	 * 
	 * @param bookReturned - Subscription status
	 * @return List of subscription
	 */
	List<Subscription> findByBookReturned(boolean bookReturned);
}
