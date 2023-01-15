package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TicketDataMapper {

	private final ModelMapper modelMapper;
	private final Converter<Long, BusTrip> busTripIdToBusTripConverter = r -> BusTrip.builder()
			.id(r.getSource())
			.build();
	private final Converter<BusTrip, Long> busTripToBusTripIdConverter = r -> r.getSource().getId();
	private final Converter<BusTrip, GetBusTripResponseDto> busTripToDtoConverter;

	public TicketDataMapper(BusTripDataMapper busTripDataMapper) {
		modelMapper = new ModelMapper();
		busTripToDtoConverter = r -> busTripDataMapper.toGetBusTripResponseDto(r.getSource());

		modelMapper.createTypeMap(BasicTicketRequestDto.class, Ticket.class)
				.addMappings(mapper -> mapper.using(busTripIdToBusTripConverter).map(
						BasicTicketRequestDto::getBusTrip, Ticket::setBusTrip
				));

		modelMapper.createTypeMap(Ticket.class, GetTicketResponseDto.class)
				.addMappings(mapper -> mapper.using(busTripToDtoConverter).map(
						Ticket::getBusTrip, GetTicketResponseDto::setBusTrip
				))
				.addMapping(t -> {
					if (t.getBusTrip() == null) return null;
					return t.getBusTrip().getBusRouteNumber();
				}, GetTicketResponseDto::setBusRouteNumber)
				.addMapping(t -> {
					if (t.getBusTrip() == null) return null;
					if (t.getBusTrip().getBus() == null) return null;
					if (t.getBusTrip().getBus().getCarrier() == null) return null;
					return t.getBusTrip().getBus().getCarrier().getName();
				}, GetTicketResponseDto::setCarrierName)
				.addMapping(t -> {
					if (t.getBusTrip() == null) return null;
					if (t.getBusTrip().getDepartureBusPoint() == null) return null;
					return t.getBusTrip().getDepartureBusPoint().getName();
				}, GetTicketResponseDto::setDepartureBusPointName)
				.addMapping(t -> {
					if (t.getBusTrip() == null) return null;
					if (t.getBusTrip().getArrivalBusPoint() == null) return null;
					return t.getBusTrip().getArrivalBusPoint().getName();
				}, GetTicketResponseDto::setArrivalBusPointName)
				.addMapping(t -> {
					if (t.getBusTrip() == null) return null;
					return t.getBusTrip().getArrivalDateTime();
				}, GetTicketResponseDto::setArrivalDateTime)
				.addMapping(t -> {
					if (t.getBusTrip() == null) return null;
					return t.getBusTrip().getDepartureDateTime();
				}, GetTicketResponseDto::setDepartureDateTime);

		modelMapper.createTypeMap(Ticket.class, GetPayedTicketResponseDto.class)
				.addMappings(mapper -> mapper.using(busTripToBusTripIdConverter).map(
						Ticket::getBusTrip, GetPayedTicketResponseDto::setBusTrip
				));

		modelMapper.createTypeMap(GetUserResponseDto.class, Ticket.class)
				.addMapping(GetUserResponseDto::getLastname, Ticket::setPassengerLastname)
				.addMapping(GetUserResponseDto::getFirstname, Ticket::setPassengerFirstname)
				.addMapping(GetUserResponseDto::getMiddlename, Ticket::setPassengerMiddlename)
				.addMapping(GetUserResponseDto::getEmail, Ticket::setEmail);

		modelMapper.createTypeMap(Ticket.class, TicketDto.class);
	}

	public GetTicketResponseDto toGetTicketResponseDto(Ticket ticket) {
		return modelMapper.map(ticket, GetTicketResponseDto.class);
	}

	public GetPayedTicketResponseDto toGetPayedTicketResponseDto(TicketCacheDto ticketCacheDto) {
		Ticket ticket = ticketCacheDto.getTicket();
		GetBusTripResponseDto busTripDto = ticketCacheDto.getBusTripDto();

		GetPayedTicketResponseDto dto = modelMapper.map(ticket, GetPayedTicketResponseDto.class);

		dto.setArrivalDateTime(
				String.format("%s %s", busTripDto.getArrivalDate(), busTripDto.getArrivalTime())
		);
		dto.setDepartureDateTime(
				String.format("%s %s", busTripDto.getDepartureDate(), busTripDto.getDepartureTime())
		);
		dto.setDepartureBusPointName(
				busTripDto.getDepartureBusPoint().getName()
		);
		dto.setArrivalBusPointName(
				busTripDto.getArrivalBusPoint().getName()
		);
		dto.setBusRouteNumber(
				busTripDto.getBusRouteNumber()
		);
		dto.setCarrierName(busTripDto.getCarrier().getName());

		return dto;
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

}
