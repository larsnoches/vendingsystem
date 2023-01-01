package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Seat;
import org.cyrilselyanin.vendingsystem.regularbus.dto.seat.BasicSeatRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.seat.GetSeatResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.helper.SeatDataMapper;
import org.cyrilselyanin.vendingsystem.regularbus.repository.vending.SeatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SeatServiceImpl implements SeatService {
	private static final String SEAT_NOT_FOUND_MESSAGE = "Место %d не найдено в базе данных.";
	private static final String SEAT_NOT_FOUND_LOG_MESSAGE = "Seat {} not found in the database.";

	private final SeatRepository seatRepo;
	private final SeatDataMapper seatDataMapper;

	@Override
	public void createSeat(BasicSeatRequestDto dto) {
		log.info("Create seat {}", dto.getName());
		Seat seat = seatDataMapper.fromBasicSeatRequestDto(dto);
		seatRepo.save(seat);
	}

	@Override
	public List<Seat> createSeats(Integer count, Long busTripId) {
		log.info("Create {} seats for busTrip {}", count, busTripId);
		List<Seat> seatList = IntStream.rangeClosed(1, count)
				.boxed()
				.map(i -> {
					Seat seat = new Seat();
					seat.setName(i.toString());
					seat.setBusTrip(BusTrip.builder()
							.id(busTripId)
							.build()
					);
					return seat;
				})
				.toList();
		return seatRepo.saveAll(seatList);
	}

	@Override
	public GetSeatResponseDto getSeat(Long id) {
		log.info("Fetching seat {}", id);
		return seatRepo.findById(id)
				.map(seatDataMapper::toGetSeatResponseDto)
				.orElseThrow(() -> {
					log.error(SEAT_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(SEAT_NOT_FOUND_MESSAGE, id)
					);
				});
	}

	@Override
	public Page<GetSeatResponseDto> getSeatsByBusTripId(Long busTripId, Pageable pageable) {
		log.info("Fetching all seats by busTrip id {}", busTripId);
		Page<Seat> seatPage = seatRepo.findAllByBusTripId(busTripId, pageable);
		List<GetSeatResponseDto> list = seatPage.stream()
				.map(seatDataMapper::toGetSeatResponseDto)
				.toList();
		return new PageImpl<>(list, pageable, seatPage.getTotalElements());
	}

	@Override
	public List<GetSeatResponseDto> getSeats(Long busTripId, Long busId, String dateTime) {
		log.info("Fetching all seats by busTrip Id {} datetime {} and bus id {}",
				busTripId, dateTime, busId
		);
		String datetimePattern = "dd.MM.yyyy HH:mm";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datetimePattern);
		LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(dateTime));
		Timestamp timestamp = Timestamp.valueOf(localDateTime);

		List<Seat> prevSeatList = seatRepo
				.findAllByBusTripArrivalDateTimeGreaterThanAndBusTripBusId(
						timestamp,
						busId
				);

		return seatRepo.findAllByBusTripId(busTripId).stream()
				.map(seatDataMapper::toGetSeatResponseDto)
				.map(seat -> {
					prevSeatList.forEach(prevSeat -> {
						if (prevSeat.getId().equals(seat.getId()) &&
								prevSeat.getSeatIsOccupied()) {
							seat.setSeatIsOccupied(true);
						}
					});
					return seat;
				})
				.toList();
	}

	@Override
	public void updateSeat(Long id, BasicSeatRequestDto dto) {
		log.info("Saving exist seat {} to the database", dto.getName());
		Seat exist = seatRepo.findById(id)
				.orElseThrow(() -> {
					log.error(SEAT_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(SEAT_NOT_FOUND_MESSAGE, id)
					);
				});

		seatDataMapper.fromBasicSeatRequestDto(dto, exist);
	}

	@Override
	public void updateSeatsWhenBusUpdated(Long busTripId, Integer count) {
		log.info(
				"Updating seats cause bus updated for busTrip {} with count {}",
				busTripId, count
		);
		List<Seat> seats = seatRepo.findAllByBusTripId(busTripId);
		seatRepo.deleteAll(seats);

		List<Seat> createdSeats = createSeats(count, busTripId);
		createdSeats.forEach(seat -> {
			seat.setSeatIsOccupied(
					seats.stream().anyMatch(oldSeat ->
							oldSeat.getName().equals(seat.getName()) &&
									oldSeat.getSeatIsOccupied()
					)
			);
		});
	}

	@Override
	public void removeSeat(Long id) {
		log.info("Removing seat {}", id);
		seatRepo.findById(id)
				.orElseThrow(() -> {
					log.error(SEAT_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(SEAT_NOT_FOUND_MESSAGE, id)
					);
				});
		seatRepo.deleteById(id);
	}

	@Override
	public void removeSeats(List<Long> seatsIds) {
		log.info("Removing seats with ids {}", seatsIds);
		seatRepo.deleteAllById(seatsIds);
	}
}
