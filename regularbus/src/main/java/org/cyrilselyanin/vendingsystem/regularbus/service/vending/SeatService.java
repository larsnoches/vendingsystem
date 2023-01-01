package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Seat;
import org.cyrilselyanin.vendingsystem.regularbus.dto.seat.BasicSeatRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.seat.GetSeatResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface SeatService {

	@PreAuthorize("hasRole('MANAGER')")
	void createSeat(BasicSeatRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	List<Seat> createSeats(Integer count, Long busTripId);

	@PreAuthorize("hasRole('MANAGER')")
	GetSeatResponseDto getSeat(Long id);

	@PreAuthorize("hasRole('MANAGER')")
	Page<GetSeatResponseDto> getSeatsByBusTripId(Long busTripId, Pageable pageable);

	List<GetSeatResponseDto> getSeats(Long busTripId, Long busId, String dateTime);

	@PreAuthorize("hasRole('MANAGER')")
	void updateSeat(Long id, BasicSeatRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	void updateSeatsWhenBusUpdated(Long busTripId, Integer count);

	@PreAuthorize("hasRole('MANAGER')")
	void removeSeat(Long id);

	@PreAuthorize("hasRole('MANAGER')")
	void removeSeats(List<Long> seatsIds);

}
