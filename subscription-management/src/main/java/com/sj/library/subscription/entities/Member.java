package com.sj.library.subscription.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Member entity
 * 
 * @author Sahil Jain
 *
 */
@Entity
@Data
public class Member implements Serializable {
	private static final long serialVersionUID = -7885235442528436445L;
	@Id
	@Column(name = "member_id", nullable = false)
	@NotNull(message = "Member ID is mandatory")
	private int memberId;
	@Column(name = "member_name", nullable = false)
	@NotNull(message = "Member name is mandatory")
	private String memberName;
	@Column(name = "membership_start_date", nullable = false)
	@NotNull(message = "Membership start date is mandatory")
	private Date membershipStartDate;
	@Column(name = "membership_status", nullable = false)
	private boolean membershipStatus = true;
}
