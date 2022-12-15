package org.cyrilselyanin.vendingsystem.regularbus.controller.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.AuthenticationRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.AuthenticationResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.helper.JwtUtils;
import org.cyrilselyanin.vendingsystem.regularbus.service.auth.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private static final String ERROR_WHEN_AUTHENTICATE_MESSAGE = "Возникла некоторая ошибка при аутентификации.";
	private static final String ERROR_WHEN_AUTHENTICATE_REFRESH_MESSAGE = "Возникла некоторая ошибка при проверке аутентификации.";

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtUtils jwtUtils;
	private final UserService userService;

	@PostMapping("/authenticate")
	public AuthenticationResponseDto authenticate(
			@RequestBody @Valid AuthenticationRequestDto requestDto
	) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						requestDto.getEmail(),
						requestDto.getPassword()
				));
		final UserDetails user = userDetailsService.loadUserByUsername(requestDto.getEmail());
		if (user != null) {
			return AuthenticationResponseDto.builder()
					.accessToken(jwtUtils.generateToken(user, 12))
					.refreshToken(jwtUtils.generateToken(user, 24))
					.build();
//			return jwtUtils.generateToken(user);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_WHEN_AUTHENTICATE_MESSAGE);
	}

	@GetMapping("/token/refresh")
	public AuthenticationResponseDto refreshToken(HttpServletRequest request) {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith("Bearer")) {
			log.error("Error with refresh null token");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_WHEN_AUTHENTICATE_REFRESH_MESSAGE);
		}

		try {
			final String jwtToken = authHeader.substring(7);
			final String username = jwtUtils.extractUsername(jwtToken);

			if (username == null) {
				log.error("Error with refresh token null username");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_WHEN_AUTHENTICATE_REFRESH_MESSAGE);
			}

			UserDetails user = userDetailsService.loadUserByUsername(username);
			return AuthenticationResponseDto.builder()
					.accessToken(jwtUtils.generateToken(user, 12))
					.refreshToken(jwtToken)
//					.refreshToken(jwtUtils.generateToken(user, 24))
					.build();
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
			log.error("Error with refresh token");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<RegistrationResponseDto> registerUser(
			@RequestBody @Valid RegistrationRequestDto requestDto
	) {
		try {
			URI uri = URI.create(
					ServletUriComponentsBuilder.fromCurrentContextPath()
							.path("/api/v1/auth/register")
							.toUriString()
			);
			return ResponseEntity.created(uri).body(
					userService.registerUser(requestDto)
			);
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

}
