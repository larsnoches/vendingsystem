package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.BasicTicketRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetTicketResponseDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class TicketDataMapper {

	private final ModelMapper modelMapper;

	private static final String datetimePattern = "dd.MM.yyyy HH:mm";
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datetimePattern);
	private final Converter<Long, BusTrip> busTripIdToBusTripConverter = r -> BusTrip.builder()
			.id(r.getSource())
			.build();
	private final Converter<BusTrip, GetBusTripResponseDto> busTripToDtoConverter;

	public TicketDataMapper(BusTripDataMapper busTripDataMapper, UserDataMapper userDataMapper) {
		modelMapper = new ModelMapper();
		busTripToDtoConverter = r -> busTripDataMapper.toGetBusTripResponseDto(r.getSource());
//		userToDtoConverter = r -> userDataMapper.toGetUserResponseDto(r.getSource());

		modelMapper.createTypeMap(BasicTicketRequestDto.class, Ticket.class)
				.addMappings(mapper -> mapper.using(busTripIdToBusTripConverter).map(
						BasicTicketRequestDto::getBusTrip, Ticket::setBusTrip
				));

		modelMapper.createTypeMap(Ticket.class, GetTicketResponseDto.class)
				.addMappings(mapper -> mapper.using(busTripToDtoConverter).map(
						Ticket::getBusTrip, GetTicketResponseDto::setBusTrip
				));

		modelMapper.createTypeMap(GetUserResponseDto.class, Ticket.class)
				.addMapping(GetUserResponseDto::getLastname, Ticket::setPassengerLastname)
				.addMapping(GetUserResponseDto::getFirstname, Ticket::setPassengerFirstname)
				.addMapping(GetUserResponseDto::getMiddlename, Ticket::setPassengerMiddlename)
				.addMapping(GetUserResponseDto::getEmail, Ticket::setEmail);

		modelMapper.createTypeMap(GetBusTripResponseDto.class, Ticket.class)
				.addMapping(GetBusTripResponseDto::getBusRouteNumber, Ticket::setBusRouteNumber)
				.addMapping(bt -> bt.getCarrier().getName(), Ticket::setCarrierName)
				.addMapping(bt -> bt.getDepartureBusPoint().getName(), Ticket::setDepartureBuspointName)
				.addMapping(bt -> bt.getArrivalBusPoint().getName(), Ticket::setArrivalBuspointName)
				.addMapping(bt -> bt.getFare().getPrice(), Ticket::setPrice);
	}

	public GetTicketResponseDto toGetTicketResponseDto(Ticket ticket) {
		return modelMapper.map(ticket, GetTicketResponseDto.class);
	}

	public Ticket fromBasicTicketRequestDto(BasicTicketRequestDto dto) {
		return modelMapper.map(dto, Ticket.class);
	}

	public void fromBasicTicketRequestDto(BasicTicketRequestDto dto, Ticket ticket) {
		modelMapper.map(dto, ticket);
	}

	public void fromGetUserResponseDto(GetUserResponseDto userDto, Ticket ticket) {
		modelMapper.map(userDto, ticket);
	}

	public void fromGetBusTripResponseDto(GetBusTripResponseDto busTripDto, Ticket ticket) {
		modelMapper.map(busTripDto, ticket);
	}

}
