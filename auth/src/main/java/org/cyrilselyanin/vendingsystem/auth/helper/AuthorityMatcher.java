package org.cyrilselyanin.vendingsystem.auth.helper;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
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

}
