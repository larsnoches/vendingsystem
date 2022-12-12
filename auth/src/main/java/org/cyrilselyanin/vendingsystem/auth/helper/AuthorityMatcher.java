package org.cyrilselyanin.vendingsystem.auth.helper;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@NoArgsConstructor
@Component
@Slf4j
public class AuthorityMatcher {

	public Boolean isManager(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream()
				.anyMatch(grantedAuthority -> {
					log.debug("from enum: " + Authority.MANAGER.name());
					log.debug("from collection: " + grantedAuthority.getAuthority());
					return grantedAuthority.getAuthority().equals(
							Authority.MANAGER.name()
					);
				});
	}

	public Boolean isManager() {
		final Authentication authentication = SecurityContextHolder
				.getContext()
				.getAuthentication();
		return authentication.getAuthorities().stream()
				.anyMatch(a -> {
					log.debug("from enum: " + Authority.MANAGER.name());
					log.debug("from collection: " + a.getAuthority());
					return a.getAuthority().equals(
							Authority.MANAGER.name()
					);
				});
	}

	public Boolean isUser() {
		final Authentication authentication = SecurityContextHolder
				.getContext()
				.getAuthentication();
		return authentication.getAuthorities().stream()
				.anyMatch(a -> {
					log.debug("from enum: " + Authority.USER.name());
					log.debug("from collection: " + a.getAuthority());
					return a.getAuthority().equals(
							Authority.USER.name()
					);
				});
	}

	public String getUsername() {
		final Authentication authentication = SecurityContextHolder
				.getContext()
				.getAuthentication();
		return authentication.getName();
	}

	public Boolean isThatUser(String username) {
		log.debug("from getUsername: " + getUsername());
		log.debug("from params: " + username);
		return getUsername().equals(username);
	}

}
