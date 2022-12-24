package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Fare;
import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.BasicFareRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.GetFareResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.helper.FareDataMapper;
import org.cyrilselyanin.vendingsystem.regularbus.repository.vending.FareRepository;
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
public class FareServiceImpl implements FareService {

	private static final String FARE_NOT_FOUND_MESSAGE = "Тариф %d не найден в базе данных.";
	private static final String FARE_NOT_FOUND_LOG_MESSAGE = "Fare {} not found in the database.";

	private final FareRepository fareRepo;
	private final FareDataMapper fareDataMapper;

	@Override
	public void createFare(BasicFareRequestDto dto) {
		log.info("Create fare {}", dto.getName());
		Fare fare = fareDataMapper.fromBasicFareRequestDto(dto);
		fareRepo.save(fare);
	}

	@Override
	public GetFareResponseDto getFare(Long id) {
		log.info("Fetching fare {}", id);
		return fareRepo.findById(id)
				.map(fareDataMapper::toGetFareResponseDto)
				.orElseThrow(() -> {
					log.error(FARE_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(FARE_NOT_FOUND_MESSAGE, id)
					);
				});
	}

	@Override
	public Page<GetFareResponseDto> getFareesByCarrierId(Long carrierId, Pageable pageable) {
		log.info("Fetching all fare");
		Page<Fare> farePage = fareRepo.findAllByCarrierId(carrierId, pageable);
		List<GetFareResponseDto> list = farePage.stream()
				.map(fareDataMapper::toGetFareResponseDto)
				.toList();
		return new PageImpl<>(list, pageable, farePage.getTotalElements());
	}

	@Override
	public void updateFare(Long id, BasicFareRequestDto dto) {
		log.info("Saving exist fare {} to the database", dto.getName());
		Fare exist = fareRepo.findById(id)
				.orElseThrow(() -> {
					log.error(FARE_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(FARE_NOT_FOUND_MESSAGE, id)
					);
				});

		fareDataMapper.fromBasicFareRequestDto(dto, exist);
	}

	@Override
	public void removeFare(Long id) {
		log.info("Removing fare {}", id);
		fareRepo.findById(id)
				.orElseThrow(() -> {
					log.error(FARE_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(FARE_NOT_FOUND_MESSAGE, id)
					);
				});
		fareRepo.deleteById(id);
	}
}
