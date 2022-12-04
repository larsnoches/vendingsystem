package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		@SuppressWarnings("unchecked")
		List roles = (ArrayList) jwt.getClaims().get("roles");

		if (roles == null || roles.isEmpty()) {
			return new ArrayList<>();
		}

		return (Collection<GrantedAuthority>)roles.stream()
				.map(r -> new SimpleGrantedAuthority((String) r))
				.collect(Collectors.toList());
	}

}
