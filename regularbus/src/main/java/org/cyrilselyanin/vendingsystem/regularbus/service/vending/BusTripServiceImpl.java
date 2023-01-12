package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Seat;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.GetBusResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.BasicBusTripRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.helper.BusTripDataMapper;
import org.cyrilselyanin.vendingsystem.regularbus.repository.vending.BusTripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BusTripServiceImpl implements BusTripService {

	private static final String BUSTRIP_NOT_FOUND_MESSAGE = "Маршрут %d не найден в базе данных.";
	private static final String BUSTRIP_NOT_FOUND_LOG_MESSAGE = "Bustrip {} not found in the database.";

	private final BusTripRepository busTripRepo;
	private final BusTripDataMapper busTripDataMapper;
	private final SeatService seatService;
	private final BusService busService;

	@Override
	public void createBusTrip(BasicBusTripRequestDto dto) {
		log.info("Create busTrip {}", dto.getBusRouteNumber());
		BusTrip busTrip = busTripDataMapper.fromBasicBusTripRequestDto(dto);
		busTripRepo.save(busTrip);

		GetBusResponseDto bus = busService.getBus(dto.getBus());
		Integer seatCount = bus.getSeatCount();

		Long busTripId = busTrip.getId();
		seatService.createSeats(seatCount, busTripId);
	}

	@Override
	public GetBusTripResponseDto getBusTrip(Long id) {
		log.info("Fetching busTrip {}", id);
		return busTripRepo.findById(id)
				.map(busTripDataMapper::toGetBusTripResponseDto)
				.orElseThrow(() -> {
					log.error(BUSTRIP_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUSTRIP_NOT_FOUND_MESSAGE, id)
					);
				});
	}

	@Override
	public Page<GetBusTripResponseDto> getBusTripsByCarrierId(Long carrierId, Pageable pageable) {
		log.info("Fetching all busTrips for carrier {}", carrierId);
		Page<BusTrip> busTripPage = busTripRepo.findAllByBusCarrierId(carrierId, pageable);
		List<GetBusTripResponseDto> list = busTripPage.stream()
				.map(busTripDataMapper::toGetBusTripResponseDto)
				.toList();
		return new PageImpl<>(list, pageable, busTripPage.getTotalElements());
	}

	@Override
	public void updateBusTrip(Long id, BasicBusTripRequestDto dto) {
		log.info("Saving exist busTrip {} to the database", dto.getBusRouteNumber());
		BusTrip exist = busTripRepo.findById(id)
				.orElseThrow(() -> {
					log.error(BUSTRIP_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUSTRIP_NOT_FOUND_MESSAGE, id)
					);
				});
		boolean shouldUpdateSeats = !dto.getBus().equals(exist.getBus().getId());
		busTripDataMapper.fromBasicBusTripRequestDto(dto, exist);

		if (shouldUpdateSeats) {
			GetBusResponseDto bus = busService.getBus(dto.getBus());
			Integer seatCount = bus.getSeatCount();
			seatService.updateSeatsWhenBusUpdated(id, seatCount);
		}
	}

	@Override
	public void removeBusTrip(Long id) {
		log.info("Removing busTrip {}", id);
		BusTrip busTrip = busTripRepo.findById(id)
				.orElseThrow(() -> {
					log.error(BUSTRIP_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUSTRIP_NOT_FOUND_MESSAGE, id)
					);
				});
		List<Long> seatsIds = busTrip.getSeats().stream()
				.map(Seat::getId)
				.toList();
		busTripRepo.deleteById(id);
		seatService.removeSeats(seatsIds);
	}

	@Override
	public Page<GetBusTripResponseDto> getBusTripsByArrivalAndDateTime(
			String busPointName, String departureDateString, Pageable pageable
	) {
		log.info("Fetching busTrips by {} and {}", busPointName, departureDateString);
		Timestamp departureDateTime = createDateTimeFromDateString(departureDateString);
		Page<BusTrip> busTripPage = busTripRepo
				.findAllByArrivalBusPointNameContainsIgnoreCaseAndDepartureDateTimeGreaterThanEqual(
						busPointName, departureDateTime, pageable
				);
		List<GetBusTripResponseDto> list = busTripPage.stream()
				.map(busTripDataMapper::toGetBusTripResponseDto)
				.toList();
		return new PageImpl<>(list, pageable, busTripPage.getTotalElements());
	}

	private Timestamp createDateTimeFromDateString(String dateString) {
		String dateTimePattern = "dd.MM.yyyy HH:mm";
		String dateTimeString = String.format("%s %s", dateString, "00:00");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
		LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(dateTimeString));
		return Timestamp.valueOf(localDateTime);
	}
}
