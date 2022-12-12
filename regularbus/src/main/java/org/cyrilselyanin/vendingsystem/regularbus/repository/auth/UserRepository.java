package org.cyrilselyanin.vendingsystem.regularbus.repository.auth;

import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

}
