package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
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

	@Override
	public BusTrip createBusTrip(BasicBusTripRequestDto dto) {
		log.info("Create busTrip {}", dto.getBusRouteNumber());
		BusTrip busTrip = busTripDataMapper.fromBasicBusTripRequestDto(dto);
		return busTripRepo.save(busTrip);
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
		Page<BusTrip> busTripPage = busTripRepo.findAllByCarrierId(carrierId, pageable);
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

		busTripDataMapper.fromBasicBusTripRequestDto(dto, exist);
	}

	@Override
	public void removeBusTrip(Long id) {
		log.info("Removing busTrip {}", id);
		busTripRepo.findById(id)
				.orElseThrow(() -> {
					log.error(BUSTRIP_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUSTRIP_NOT_FOUND_MESSAGE, id)
					);
				});
		busTripRepo.deleteById(id);
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
