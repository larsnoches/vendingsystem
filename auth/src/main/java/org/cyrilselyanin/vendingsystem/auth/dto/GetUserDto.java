package org.cyrilselyanin.vendingsystem.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Get user response DTO
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetUserDto implements Serializable {

	@NotBlank(message = "Имя пользователя не может быть пустым.")
	@Size(
			min = 2,
			max = 50,
			message = "Имя пользователя должно быть от 2 до 50 символов.")
	private String username;

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

//	@Size(
//			max = 255,
//			message = "Email должен быть не более 255 символов")
//	private String email;
//
//	@Size(
//			max = 20,
//			message = "Номер телефона должен быть не более 20 символов")
//	private String phone;
//
//	private Boolean isManager;


//	public static GetUserDto fromUserAndProfile(User user, Profile profile, Boolean isManager) {
//		GetUserDto dto = new GetUserDto();
//		dto.setId(user.getId());
//		dto.setUsername(user.getUsername());
//		dto.setLastname(profile.getLastname());
//		dto.setFirstname(profile.getFirstname());
//		dto.setMiddlename(profile.getMiddlename());
//		dto.setEmail(profile.getEmail());
//		dto.setPhone(profile.getPhone());
//		dto.setIsManager(isManager);
//		return dto;
//	}

}
