package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.BasicBusRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.GetBusResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface BusService {

	@PreAuthorize("hasRole('MANAGER')")
	void createBus(BasicBusRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	GetBusResponseDto getBus(Long id);

	@PreAuthorize("hasRole('MANAGER')")
	Page<GetBusResponseDto> getBusesByCarrierId(Long carrierId, Pageable pageable);

	@PreAuthorize("hasRole('MANAGER')")
	void updateBus(Long id, BasicBusRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	void removeBus(Long id);

}
