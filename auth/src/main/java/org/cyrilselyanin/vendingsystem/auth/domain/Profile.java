package org.cyrilselyanin.vendingsystem.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile {
	@Id
	@Column(name = "username", nullable = false)
	private String username;

	@Size(
		min = 2,
		max = 255,
		message = "Lastname must be between 2 and 255 characters")
	@Column(name = "lastname", nullable = false)
	private String lastname;

	@Size(
			min = 2,
			max = 255,
			message = "Firstname must be between 2 and 255 characters")
	@Column(name = "firstname", nullable = false)
	private String firstname;

	@Size(
			min = 2,
			max = 255,
			message = "Middlename must be between 2 and 255 characters")
	@Column(name = "middlename", nullable = false)
	private String middlename;

	@Size(
			min = 2,
			max = 255,
			message = "Email must be between 2 and 255 characters")
	@Column(name = "email", nullable = false)
	private String email;

	@Size(
			max = 20,
			message = "Phone must be between 2 and 255 characters")
	@Column(name = "phone", length = 20)
	private String phone;
}
