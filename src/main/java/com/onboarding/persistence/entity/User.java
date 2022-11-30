package com.onboarding.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@DynamicUpdate
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "users")
public class User extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Lob
	@Column(name = "profile_img", length = 1000)
	private String profileImage;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "password")
	private String password;

	@Column(name = "provider")
	private String provider;

	@Column(name = "provider_id")
	private String providerId;

	@Column(columnDefinition = "tinyint(1) default 0", name = "is_blocked")
	private boolean blocked;



}
