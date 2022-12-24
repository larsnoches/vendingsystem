package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusPoint;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.BasicBusPointRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.GetBusPointResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.helper.BusPointDataMapper;
import org.cyrilselyanin.vendingsystem.regularbus.repository.vending.BusPointRepository;
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
public class BusPointServiceImpl implements BusPointService {

	private static final String BUS_POINT_NOT_FOUND_MESSAGE = "Остановочный пункт %d не найден в базе данных.";
	private static final String BUS_POINT_NOT_FOUND_LOG_MESSAGE = "Buspoint {} not found in the database.";

	private final BusPointRepository busPointRepo;
	private final BusPointDataMapper busPointDataMapper;

	@Override
	public void createBusPoint(BasicBusPointRequestDto dto) {
		log.info("Create buspoint {}", dto.getName());
		BusPoint busPoint = busPointDataMapper.fromBasicBusPointRequestDto(dto);
		busPointRepo.save(busPoint);
	}

	@Override
	public GetBusPointResponseDto getBusPoint(Long id) {
		log.info("Fetching buspoint {}", id);
		return busPointRepo.findById(id)
				.map(busPointDataMapper::toGetBusPointResponseDto)
				.orElseThrow(() -> {
					log.error(BUS_POINT_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUS_POINT_NOT_FOUND_MESSAGE, id)
					);
				});
	}

	@Override
	public Page<GetBusPointResponseDto> getBusPoints(Pageable pageable) {
		log.info("Fetching all buspoints");
		Page<BusPoint> busPointPage = busPointRepo.findAll(pageable);
		List<GetBusPointResponseDto> list = busPointPage.stream()
				.map(busPointDataMapper::toGetBusPointResponseDto)
				.toList();
		return new PageImpl<>(list, pageable, busPointPage.getTotalElements());
	}

	@Override
	public void updateBusPoint(Long id, BasicBusPointRequestDto dto) {
		log.info("Saving exist buspoint {} to the database", dto.getName());
		BusPoint exist = busPointRepo.findById(id)
				.orElseThrow(() -> {
					log.error(BUS_POINT_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUS_POINT_NOT_FOUND_MESSAGE, id)
					);
				});
		busPointDataMapper.fromBasicBusPointRequestDto(dto, exist);
	}

	@Override
	public void removeBusPoint(Long id) {
		log.info("Removing buspoint {}", id);
		busPointRepo.findById(id)
				.orElseThrow(() -> {
					log.error(BUS_POINT_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(BUS_POINT_NOT_FOUND_MESSAGE, id)
					);
				});
		busPointRepo.deleteById(id);
	}

}
