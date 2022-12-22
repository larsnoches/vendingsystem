package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.BasicBusTripRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface BusTripService {

	@PreAuthorize("hasRole('MANAGER')")
	GetBusTripResponseDto createBusTrip(BasicBusTripRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	GetBusTripResponseDto getBusTrip(Long id);

	@PreAuthorize("hasRole('MANAGER')")
	Page<GetBusTripResponseDto> getBusTripTripsByCarrierId(Long carrierId, Pageable pageable);

	@PreAuthorize("hasRole('MANAGER')")
	GetBusTripResponseDto updateBusTrip(Long id, BasicBusTripRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	void removeBusTrip(Long id);

}
