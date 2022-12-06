package org.cyrilselyanin.vendingsystem.auth.repository;

import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Vending system user profile repository
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
