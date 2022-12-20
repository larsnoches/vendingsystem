package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.cyrilselyanin.vendingsystem.regularbus.domain.Carrier;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.BasicCarrierRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.GetCarrierResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CarrierDataMapper {

	private final ModelMapper modelMapper;

	public CarrierDataMapper() {
		modelMapper = new ModelMapper();
	}

	public GetCarrierResponseDto toGetCarrierResponseDto(Carrier carrier) {
		return modelMapper.map(carrier, GetCarrierResponseDto.class);
	}

	public Carrier fromBasicCarrierRequestDto(BasicCarrierRequestDto dto) {
		return modelMapper.map(dto, Carrier.class);
	}

	public void fromBasicCarrierRequestDto(BasicCarrierRequestDto dto, Carrier carrier) {
		modelMapper.map(dto, carrier);
	}

}
