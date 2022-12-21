package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.BasicBusPointRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.GetBusPointResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface BusPointService {

	@PreAuthorize("hasRole('MANAGER')")
	GetBusPointResponseDto createBusPoint(BasicBusPointRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	GetBusPointResponseDto getBusPoint(Long id);

	@PreAuthorize("hasRole('MANAGER')")
	Page<GetBusPointResponseDto> getBusPoints(Pageable pageable);

	@PreAuthorize("hasRole('MANAGER')")
	GetBusPointResponseDto updateBusPoint(Long id, BasicBusPointRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	void removeBusPoint(Long id);

}
