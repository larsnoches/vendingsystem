package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Seat;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.seat.BasicSeatRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.seat.GetSeatResponseDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SeatDataMapper {

	private final ModelMapper modelMapper;
	private final Converter<Long, BusTrip> busTripIdToBusTripConverter = r -> BusTrip.builder()
			.id(r.getSource())
			.build();
	private final Converter<BusTrip, GetBusTripResponseDto> busTripToDtoConverter;

	public SeatDataMapper(BusTripDataMapper busTripDataMapper) {
		modelMapper = new ModelMapper();
		busTripToDtoConverter = r -> busTripDataMapper.toGetBusTripResponseDto(r.getSource());

		modelMapper.createTypeMap(BasicSeatRequestDto.class, Seat.class)
				.addMappings(mapper -> mapper.using(busTripIdToBusTripConverter).map(
						BasicSeatRequestDto::getBusTrip, Seat::setBusTrip
				));

		modelMapper.createTypeMap(Seat.class, GetSeatResponseDto.class)
				.addMappings(mapper -> mapper.using(busTripToDtoConverter).map(
						Seat::getBusTrip, GetSeatResponseDto::setBusTrip
				));
	}

	public GetSeatResponseDto toGetSeatResponseDto(Seat seat) {
		return modelMapper.map(seat, GetSeatResponseDto.class);
	}

	public Seat fromBasicSeatRequestDto(BasicSeatRequestDto dto) {
		return modelMapper.map(dto, Seat.class);
	}

	public void fromBasicSeatRequestDto(BasicSeatRequestDto dto, Seat seat) {
		modelMapper.map(dto, seat);
	}

}
