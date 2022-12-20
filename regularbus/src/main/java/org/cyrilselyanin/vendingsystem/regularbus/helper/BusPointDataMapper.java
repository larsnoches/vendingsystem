package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusPoint;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusPointType;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.GetBusPointResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.BasicBusPointRequestDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BusPointDataMapper {

	private final ModelMapper modelMapper;
	private final Converter<BusPointType, String> busPointTypeToStringConverter = r -> r
			.getSource()
			.name();

	private final Converter<String, BusPointType> stringToBusPointTypeConverter = r -> BusPointType
			.valueOf(r.getSource());


	public BusPointDataMapper() {
		modelMapper = new ModelMapper();

		modelMapper.createTypeMap(BusPoint.class, GetBusPointResponseDto.class)
				.addMappings(mapper -> mapper.using(busPointTypeToStringConverter).map(
						BusPoint::getBusPointType, GetBusPointResponseDto::setBusPointType
				));
		modelMapper.createTypeMap(BasicBusPointRequestDto.class, BusPoint.class)
				.addMappings(mapper -> mapper.using(stringToBusPointTypeConverter).map(
						BasicBusPointRequestDto::getBusPointType, BusPoint::setBusPointType
				));
	}

	public GetBusPointResponseDto toGetBusPointResponseDto(BusPoint busPoint) {
		return modelMapper.map(busPoint, GetBusPointResponseDto.class);
	}

	public BusPoint fromBasicBusPointRequestDto(BasicBusPointRequestDto dto) {
		return modelMapper.map(dto, BusPoint.class);
	}

}
