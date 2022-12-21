package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Bus;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Carrier;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.BasicBusRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.GetBusResponseDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BusDataMapper {

	private final ModelMapper modelMapper;
	private final Converter<Long, Carrier> carrierIdToCarrierConverter = r -> new Carrier(
			r.getSource(), null, null, null, null, null
	);

	public BusDataMapper() {
		modelMapper = new ModelMapper();

		modelMapper.createTypeMap(BasicBusRequestDto.class, Bus.class)
				.addMappings(mapper -> mapper.using(carrierIdToCarrierConverter).map(
						BasicBusRequestDto::getCarrier, Bus::setCarrier
				));
	}

	public GetBusResponseDto toGetBusResponseDto(Bus bus) {
		return modelMapper.map(bus, GetBusResponseDto.class);
	}

	public Bus fromBasicBusRequestDto(BasicBusRequestDto dto) {
		return modelMapper.map(dto, Bus.class);
	}

	public void fromBasicBusRequestDto(BasicBusRequestDto dto, Bus bus) {
		modelMapper.map(dto, bus);
	}

}
