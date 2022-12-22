package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Carrier;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Fare;
import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.BasicFareRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.GetFareResponseDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FareDataMapper {

	private final ModelMapper modelMapper;
	private final Converter<Long, Carrier> carrierIdToCarrierConverter = r -> new Carrier(
			r.getSource(), null, null, null, null, null, null
	);
	private final Converter<Carrier, Long> carrierToCarrierIdConverter = r -> r.getSource().getId();

	public FareDataMapper() {
		modelMapper = new ModelMapper();

		modelMapper.createTypeMap(BasicFareRequestDto.class, Fare.class)
				.addMappings(mapper -> mapper.using(carrierIdToCarrierConverter).map(
						BasicFareRequestDto::getCarrier, Fare::setCarrier
				));

		modelMapper.createTypeMap(Fare.class, GetFareResponseDto.class)
				.addMappings(mapper -> mapper.using(carrierToCarrierIdConverter).map(
						Fare::getCarrier, GetFareResponseDto::setCarrier
				));
	}

	public GetFareResponseDto toGetFareResponseDto(Fare fare) {
		return modelMapper.map(fare, GetFareResponseDto.class);
	}

	public Fare fromBasicFareRequestDto(BasicFareRequestDto dto) {
		return modelMapper.map(dto, Fare.class);
	}

	public void fromBasicFareRequestDto(BasicFareRequestDto dto, Fare fare) {
		modelMapper.map(dto, fare);
	}

}
