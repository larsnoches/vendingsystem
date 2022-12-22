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
	public GetBusTripResponseDto createBusTrip(BasicBusTripRequestDto dto) {
		log.info("Create busTrip {}", dto.getBusRouteNumber());
		BusTrip busTrip = busTripDataMapper.fromBasicBusTripRequestDto(dto);
		return busTripDataMapper.toGetBusTripResponseDto(
				busTripRepo.save(busTrip)
		);
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
	public Page<GetBusTripResponseDto> getBusTripTripsByCarrierId(Long carrierId, Pageable pageable) {
		log.info("Fetching all busTrips");
		Page<BusTrip> busTripPage = busTripRepo.findAllByCarrierId(carrierId, pageable);
		List<GetBusTripResponseDto> list = busTripPage.stream()
				.map(busTripDataMapper::toGetBusTripResponseDto)
				.toList();
		return new PageImpl<>(list, pageable, busTripPage.getTotalElements());
	}

	@Override
	public GetBusTripResponseDto updateBusTrip(Long id, BasicBusTripRequestDto dto) {
		log.info("Saving exist busTrip {} to the database", dto.getBusRouteNumber());
		BusTrip exist = busTripRepo.findById(id)
				.orElseThrow(() -> {
					log.error(BUSTRIP_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUSTRIP_NOT_FOUND_MESSAGE, id)
					);
				});

		busTripDataMapper.fromBasicBusTripRequestDto(dto, exist);
		return busTripDataMapper.toGetBusTripResponseDto(
				exist
		);
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
}
