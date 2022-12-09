package org.cyrilselyanin.vendingsystem.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Authority {

	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "authorities_gen")
	@SequenceGenerator(
			name = "authorities_gen",
			sequenceName="authorities_seq",
			allocationSize = 1)
	@Column(name = "authority_id", nullable = false)
	private Long id;

	@NotBlank(message = "Имя пользователя не может быть пустым")
	@Column(name = "username", nullable = false)
	private String username;

	@Size(
			max = 68,
			message = "Роль должна быть не более 68 символов")
	@Column(name = "authority", nullable = false)
	private String authority;

}
