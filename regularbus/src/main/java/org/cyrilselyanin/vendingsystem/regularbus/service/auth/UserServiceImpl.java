package org.cyrilselyanin.vendingsystem.regularbus.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.User;
import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.UserRole;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.*;
import org.cyrilselyanin.vendingsystem.regularbus.helper.UserDataMapper;
import org.cyrilselyanin.vendingsystem.regularbus.repository.auth.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

	private static final String USER_NOT_FOUND_MESSAGE = "Пользователь %s не найден в базе данных.";
	private static final String USER_ALREADY_EXISTS_MESSAGE = "Такой пользователь уже зарегистрирован.";

	private final UserRepository userRepo;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserDataMapper userDataMapper;

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> {
					log.error("User {} not found in the database", email);
					throw new IllegalStateException(
							String.format(USER_NOT_FOUND_MESSAGE, email)
					);
				});
		log.info("User {} found in the database", email);
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				user.getEnabled(),
				true,
				true,
				true,
				user.getAuthorities()
		);
	}

	@Override
	public GetUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
		String email = createUserRequestDto.getEmail();
		log.info("Saving new user {} to the database", email);
		userRepo.findByEmail(email)
				.ifPresent(u -> {
					log.error("User {} is already exists in the database", email);
					throw new IllegalStateException(USER_ALREADY_EXISTS_MESSAGE);
				});

		User user = userDataMapper.fromCreateUserRequestDto(createUserRequestDto);
		return userDataMapper.toGetUserResponseDto(
				userRepo.save(user)
		);
	}

	@Override
	public GetUserResponseDto updateUserById(Long id, UpdateUserRequestDto updateUserRequestDto){
		log.info("Saving exist user {} to the database", updateUserRequestDto.getEmail());
		User exist = userRepo.findById(id)
				.orElseThrow(() -> {
					log.error("User {} not found in the database", id);
					throw new IllegalStateException(
							String.format(USER_NOT_FOUND_MESSAGE, id)
					);
				});

		User user = userDataMapper.fromUpdateUserRequestDto(updateUserRequestDto);
		user.setId(exist.getId());
		user.setPassword(exist.getPassword());
		return userDataMapper.toGetUserResponseDto(
				userRepo.save(user)
		);
	}

	@Override
	public void changePassword(Long id, ChangePasswordRequestDto changePasswordRequestDto) {
		log.info("Change pwd for exist user {}", id);
		User user = userRepo.findById(id)
				.orElseThrow(() -> {
					log.error("User {} not found in the database", id);
					throw new IllegalStateException(
							String.format(USER_NOT_FOUND_MESSAGE, id)
					);
				});
		user.setPassword(changePasswordRequestDto.getPassword());
//		userRepo.save(user);
	}

	@Override
	public GetUserResponseDto getUser(String email) {
		log.info("Fetching user {}", email);
		return userRepo.findByEmail(email)
				.map(userDataMapper::toGetUserResponseDto)
				.orElseThrow(() -> {
					log.error("User {} not found in the database", email);
					throw new IllegalStateException(
							String.format(USER_NOT_FOUND_MESSAGE, email)
					);
				});
	}

	@Override
	public GetUserResponseDto getUser(Long id) {
		log.info("Fetching user {}", id);
		return userRepo.findById(id)
				.map(userDataMapper::toGetUserResponseDto)
				.orElseThrow(() -> {
					log.error("User {} not found in the database", id);
					throw new IllegalStateException(
							String.format(USER_NOT_FOUND_MESSAGE, id)
					);
				});
	}

	@Override
	public Page<GetUserResponseDto> getUsers(Pageable pageable) {
		log.info("Fetching all users");
//		return userRepo.findAll(pageable);
		List<GetUserResponseDto> list = userRepo.findAll(pageable).stream()
				.map(userDataMapper::toGetUserResponseDto)
				.collect(Collectors.toList());
		return new PageImpl<>(list);
	}

	@Override
	public RegistrationResponseDto registerUser(RegistrationRequestDto requestDto) {
		log.info("Registration user {}", requestDto.getEmail());
		boolean userExists = userRepo
				.findByEmail(requestDto.getEmail())
				.isPresent();
		if (userExists) {
			throw new IllegalStateException(USER_ALREADY_EXISTS_MESSAGE);
		}

		User user = userDataMapper.fromRegistrationRequestDto(requestDto);
		String encodedPassword = bCryptPasswordEncoder
				.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setUserRole(UserRole.USER_ROLE);
		userRepo.save(user);

		return userDataMapper.toRegistrationResponseDto(user);
	}

	@Override
	public void removeUser(Long id) {
		log.info("Removing user {}", id);
		userRepo.findById(id)
				.orElseThrow(() -> {
					log.error("User {} not found in the database", id);
					throw new IllegalStateException(
							String.format(USER_NOT_FOUND_MESSAGE, id)
					);
				});
		userRepo.deleteById(id);
	}

}
