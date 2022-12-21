package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Bus;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.BasicBusRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.GetBusResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.helper.BusDataMapper;
import org.cyrilselyanin.vendingsystem.regularbus.repository.vending.BusRepository;
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
public class BusServiceImpl implements BusService {

	private static final String BUS_NOT_FOUND_MESSAGE = "Автобус %d не найден в базе данных.";
	private static final String BUS_NOT_FOUND_LOG_MESSAGE = "Bus {} not found in the database.";

	private final BusRepository busRepo;
	private final BusDataMapper busDataMapper;

	@Override
	public GetBusResponseDto createBus(BasicBusRequestDto dto) {
		log.info("Create buspoint {}", dto.getMakeModel());
		Bus bus = busDataMapper.fromBasicBusRequestDto(dto);
		return busDataMapper.toGetBusResponseDto(
				busRepo.save(bus)
		);
	}

	@Override
	public GetBusResponseDto getBus(Long id) {
		log.info("Fetching buspoint {}", id);
		return busRepo.findById(id)
				.map(busDataMapper::toGetBusResponseDto)
				.orElseThrow(() -> {
					log.error(BUS_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUS_NOT_FOUND_MESSAGE, id)
					);
				});
	}

	@Override
	public Page<GetBusResponseDto> getBusesByCarrierId(Long carrierId, Pageable pageable) {
		log.info("Fetching all buspoints");
		Page<Bus> busPage = busRepo.findAllByCarrierId(carrierId, pageable);
		List<GetBusResponseDto> list = busPage.stream()
				.map(busDataMapper::toGetBusResponseDto)
				.toList();
		return new PageImpl<>(list, pageable, busPage.getTotalElements());
	}

	@Override
	public GetBusResponseDto updateBus(Long id, BasicBusRequestDto dto) {
		log.info("Saving exist bus {} to the database", dto.getMakeModel());
		Bus exist = busRepo.findById(id)
				.orElseThrow(() -> {
					log.error(BUS_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUS_NOT_FOUND_MESSAGE, id)
					);
				});

		busDataMapper.fromBasicBusRequestDto(dto, exist);
		return busDataMapper.toGetBusResponseDto(
				exist
		);
	}

	@Override
	public void removeBus(Long id) {
		log.info("Removing bus {}", id);
		busRepo.findById(id)
				.orElseThrow(() -> {
					log.error(BUS_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUS_NOT_FOUND_MESSAGE, id)
					);
				});
		busRepo.deleteById(id);
	}
}
