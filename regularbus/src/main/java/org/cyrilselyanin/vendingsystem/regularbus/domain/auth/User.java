package org.cyrilselyanin.vendingsystem.regularbus.domain.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "users_gen")
	@SequenceGenerator(
			name = "users_gen",
			sequenceName="users_seq",
			allocationSize = 1)
	@Column(name = "user_id", nullable = false)
	private Long id;

	@NotBlank(message = "Адрес электронной почты не может быть пустым.")
	@Size(
			min = 6,
			max = 255,
			message = "Адрес электронной почты должен быть от 6 до 255 символов.")
	@Email(message = "Такой адрес электронной почты недопустим.")
	@Column(nullable = false)
	private String email;

	@NotBlank(message = "Пароль не может быть пустым.")
	@Size(
			min = 4,
			max = 68,
			message = "Пароль должен быть от 4 до 68 символов.")
	@Column(nullable = false)
	private String password;

	@Size(
			max = 255,
			message = "Фамилия должна быть не более 255 символов")
	private String lastname;

	@Size(
			max = 255,
			message = "Имя должно быть не более 255 символов")
	private String firstname;

	@Size(
			max = 255,
			message = "Отчество должно быть не более 255 символов")
	private String middlename;

	private Boolean enabled = true;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role", nullable = false)
	private UserRole userRole = UserRole.ROLE_USER;

//	@OneToMany(mappedBy = "user")
//	private Set<Ticket> tickets = new HashSet<>();

	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
				userRole.name()
		);
		return Collections.singleton(authority);
	}

}
