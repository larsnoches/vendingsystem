package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.BasicBusTripRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface BusTripService {

	@PreAuthorize("hasRole('MANAGER')")
	void createBusTrip(BasicBusTripRequestDto dto);

//	@PreAuthorize("hasRole('MANAGER')")
	GetBusTripResponseDto getBusTrip(Long id);

	@PreAuthorize("hasRole('MANAGER')")
	Page<GetBusTripResponseDto> getBusTripsByCarrierId(Long carrierId, Pageable pageable);

	@PreAuthorize("hasRole('MANAGER')")
	void updateBusTrip(Long id, BasicBusTripRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	void removeBusTrip(Long id);

	Page<GetBusTripResponseDto> getBusTripsByArrivalAndDateTime(
			String busPointName, String departureDateString, Pageable pageable
	);

}
