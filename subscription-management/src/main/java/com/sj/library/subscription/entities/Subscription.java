package com.sj.library.subscription.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Subscription entity
 * 
 * @author Sahil Jain
 *
 */
@Entity
@Data
public class Subscription implements Serializable {
	private static final long serialVersionUID = 8307817788371355821L;
	@Id
	@Column(name = "subscription_id", nullable = false)
	@NotNull(message = "Subscription ID is mandatory")
	private int subscriptionId;
	@Column(name = "subscription_date", nullable = false)
	@NotNull(message = "Subscription date is mandatory")
	private Date subscriptionDate;
	@Column(name = "return_date", nullable = true)
	private Date returnDate;
	@Column(name = "book_returned", nullable = true)
	private boolean bookReturned = false;
	@Column(name = "book_id", nullable = false)
	@NotNull(message = "Book ID is mandatory")
	private int bookId;

	@Transient
	private int memberId;

	@OneToOne
	@JoinColumn(name = "member_Id", referencedColumnName = "member_Id", nullable = false)
	private Member member;
}
