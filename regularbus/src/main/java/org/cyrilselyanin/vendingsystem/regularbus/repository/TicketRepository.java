package org.cyrilselyanin.vendingsystem.regularbus.repository;

import org.cyrilselyanin.vendingsystem.regularbus.domain.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @RestResource(exported = false)
    Optional<Ticket> findByIdAndUserId(Long id, String userId);

    @RestResource(exported = false)
    List<Ticket> findAllByUserId(String userId);

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @NonNull
    @Override
    Optional<Ticket> findById(@NonNull Long id);

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @NonNull
    @Override
    List<Ticket> findAll();

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @NonNull
    @Override
    List<Ticket> findAll(@NonNull Sort sort);

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @NonNull
    @Override
    Page<Ticket> findAll(@NonNull Pageable pageable);
}
