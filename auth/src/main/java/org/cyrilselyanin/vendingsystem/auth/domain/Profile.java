package org.cyrilselyanin.vendingsystem.auth.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Profile {

	@Id
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "profiles_gen")
	@SequenceGenerator(
		name = "profiles_gen",
		sequenceName="profiles_seq",
		allocationSize = 1)
	@Column(name = "profile_id", nullable = false)
	private Long id;

	@NotBlank(message = "Имя пользователя не может быть пустым")
	@Column(name = "username", nullable = false)
	private String username;

	@Size(
		max = 255,
		message = "Фамилия должна быть не более 255 символов")
	@Column(name = "lastname")
	private String lastname;

	@Size(
		max = 255,
		message = "Имя должно быть не более 255 символов")
	@Column(name = "firstname")
	private String firstname;

	@Size(
		max = 255,
		message = "Отчество должно быть не более 255 символов")
	@Column(name = "middlename")
	private String middlename;

	@Size(
		max = 255,
		message = "Email должен быть не более 255 символов")
	@Column(name = "email")
	private String email;

	@Size(
		max = 20,
		message = "Номер телефона должен быть не более 20 символов")
	@Column(name = "phone", length = 20)
	private String phone;

//	public static Profile fromUserDto(GetOrUpdateProfileDto dto) {
//		Profile p = new Profile();
//		p.username = dto.getUsername();
//		p.lastname = dto.getLastname();
//		p.firstname = dto.getFirstname();
//		p.middlename = dto.getMiddlename();
//		p.email = dto.getEmail();
//		p.phone = dto.getPhone();
//		return p;
//	}

}
