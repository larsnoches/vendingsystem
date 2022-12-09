package org.cyrilselyanin.vendingsystem.auth.repository;

import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Vending system user profile repository
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {

	Optional<Profile> findByUsername(String username);
	void deleteByUsername(String username);

}
