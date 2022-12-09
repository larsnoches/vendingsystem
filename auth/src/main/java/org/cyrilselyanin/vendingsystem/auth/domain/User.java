package org.cyrilselyanin.vendingsystem.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

	@NotBlank(message = "Имя пользователя не может быть пустым.")
	@Size(
			min = 2,
			max = 50,
			message = "Имя пользователя должно быть от 2 до 50 символов.")
	@Column(name = "username", nullable = false)
	private String username;

	@NotBlank(message = "Пароль не может быть пустым.")
	@Size(
			min = 4,
			max = 68,
			message = "Пароль должен быть от 4 до 68 символов.")
	@Column(name = "password", nullable = false)
	private String password;

//	@NotBlank(message = "Повторно введенный пароль не может быть пустым.")
//	@Size(
//			min = 4,
//			max = 68,
//			message = "Повторно введенный пароль должен быть от 4 до 68 символов.")
//	@Transient
//	private String password2;

	@Column(name = "enabled", nullable = false)
	private Boolean enabled;

}
