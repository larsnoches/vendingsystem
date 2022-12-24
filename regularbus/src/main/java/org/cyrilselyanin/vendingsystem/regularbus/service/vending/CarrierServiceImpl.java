package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Carrier;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.BasicCarrierRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.GetCarrierResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.helper.CarrierDataMapper;
import org.cyrilselyanin.vendingsystem.regularbus.repository.vending.CarrierRepository;
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
public class CarrierServiceImpl implements CarrierService {

	private static final String CARRIER_NOT_FOUND_MESSAGE = "Перевозчик %d не найден в базе данных.";
	private static final String CARRIER_NOT_FOUND_LOG_MESSAGE = "Carrier {} not found in the database.";

	private final CarrierRepository carrierRepo;
	private final CarrierDataMapper carrierDataMapper;

	@Override
	public void createCarrier(BasicCarrierRequestDto dto) {
		log.info("Create carrier {}", dto.getName());
		Carrier carrier = carrierDataMapper.fromBasicCarrierRequestDto(dto);
		carrierRepo.save(carrier);
	}

	@Override
	public GetCarrierResponseDto getCarrier(Long id) {
		log.info("Fetching carrier {}", id);
		return carrierRepo.findById(id)
				.map(carrierDataMapper::toGetCarrierResponseDto)
				.orElseThrow(() -> {
					log.error(CARRIER_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(CARRIER_NOT_FOUND_MESSAGE, id)
					);
				});
	}

	@Override
	public Page<GetCarrierResponseDto> getCarriers(Pageable pageable) {
		log.info("Fetching all carriers");
		Page<Carrier> carrierPage = carrierRepo.findAll(pageable);
		List<GetCarrierResponseDto> list = carrierPage.stream()
				.map(carrierDataMapper::toGetCarrierResponseDto)
				.toList();
		return new PageImpl<>(list, pageable, carrierPage.getTotalElements());
	}

	@Override
	public void updateCarrier(Long id, BasicCarrierRequestDto dto) {
		log.info("Saving exist carrier {} to the database", dto.getName());
		Carrier exist = carrierRepo.findById(id)
				.orElseThrow(() -> {
					log.error(CARRIER_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(CARRIER_NOT_FOUND_MESSAGE, id)
					);
				});

		carrierDataMapper.fromBasicCarrierRequestDto(dto, exist);
	}

	@Override
	public void removeCarrier(Long id) {
		log.info("Removing carrier {}", id);
		carrierRepo.findById(id)
				.orElseThrow(() -> {
					log.error(CARRIER_NOT_FOUND_LOG_MESSAGE, id);
					throw new IllegalStateException(
							String.format(CARRIER_NOT_FOUND_MESSAGE, id)
					);
				});
		carrierRepo.deleteById(id);
	}
}
