package org.cyrilselyanin.vendingsystem.regularbus.helper;

import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.*;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.GetBusResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.GetBusPointResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.BasicBusTripRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.GetCarrierResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.GetFareResponseDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class BusTripDataMapper {

	private final ModelMapper modelMapper;

	private static final String datetimePattern = "dd.MM.yyyy HH:mm";
	private static final String datePattern = "dd.MM.yyyy";
	private static final String timePattern = "HH:mm";
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datetimePattern);
	private final Converter<Timestamp, String> timestampToDateStringConverter = r ->
			new SimpleDateFormat(datePattern).format(r.getSource());
	private final Converter<Timestamp, String> timestampToTimeStringConverter = r ->
			new SimpleDateFormat(timePattern).format(r.getSource());
	private final Converter<String, Timestamp> datetimeStringToTimestampConverter = r -> {
		LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(r.getSource()));
		return Timestamp.valueOf(localDateTime);
	};

	private final Converter<BusPoint, GetBusPointResponseDto> busPointToDtoConverter;
	private final Converter<Bus, GetBusResponseDto> busToDtoConverter;
	private final Converter<Fare, GetFareResponseDto> fareToDtoConverter;
	private final Converter<Carrier, GetCarrierResponseDto> carrierToDtoConverter;

	private final Converter<Long, Carrier> carrierIdToCarrierConverter = r -> new Carrier(
			r.getSource(), null, null, null, null, null, null
	);
	private final Converter<Long, Fare> fareIdToFareConverter = r -> new Fare(
			r.getSource(), null, null, null, null
	);
	private final Converter<Long, Bus> busIdToBusConverter = r -> new Bus(
			r.getSource(), null, null, null,
			null, null, null, null
	);
	private final Converter<Long, BusPoint> busPointIdToBusPointConverter = r -> new BusPoint(
			r.getSource(), null, null, null,
			null, null
	);

	public BusTripDataMapper(
			BusPointDataMapper busPointDataMapper,
			BusDataMapper busDataMapper,
			FareDataMapper fareDataMapper,
			CarrierDataMapper carrierDataMapper
	) {
		modelMapper = new ModelMapper();
		busPointToDtoConverter = r -> busPointDataMapper.toGetBusPointResponseDto(
				r.getSource()
		);
		busToDtoConverter = r -> busDataMapper.toGetBusResponseDto(r.getSource());
		fareToDtoConverter = r -> fareDataMapper.toGetFareResponseDto(r.getSource());
		carrierToDtoConverter = r -> carrierDataMapper.toGetCarrierResponseDto(r.getSource());

		modelMapper.createTypeMap(BusTrip.class, GetBusTripResponseDto.class)
				.addMappings(mapper -> mapper.using(timestampToDateStringConverter).map(
						BusTrip::getDepartureDateTime, GetBusTripResponseDto::setDepartureDate
				))
				.addMappings(mapper -> mapper.using(timestampToTimeStringConverter).map(
						BusTrip::getDepartureDateTime, GetBusTripResponseDto::setDepartureTime
				))
				.addMappings(mapper -> mapper.using(timestampToDateStringConverter).map(
						BusTrip::getArrivalDateTime, GetBusTripResponseDto::setArrivalDate
				))
				.addMappings(mapper -> mapper.using(timestampToTimeStringConverter).map(
						BusTrip::getArrivalDateTime, GetBusTripResponseDto::setArrivalTime
				))
				.addMappings(mapper -> mapper.using(busPointToDtoConverter).map(
						BusTrip::getDepartureBusPoint, GetBusTripResponseDto::setDepartureBusPoint
				))
				.addMappings(mapper -> mapper.using(busPointToDtoConverter).map(
						BusTrip::getArrivalBusPoint, GetBusTripResponseDto::setArrivalBusPoint
				))
				.addMappings(mapper -> mapper.using(busToDtoConverter).map(
						BusTrip::getBus, GetBusTripResponseDto::setBus
				))
				.addMappings(mapper -> mapper.using(fareToDtoConverter).map(
						BusTrip::getFare, GetBusTripResponseDto::setFare
				))
				.addMappings(mapper -> mapper.using(carrierToDtoConverter).map(
						BusTrip::getCarrier, GetBusTripResponseDto::setCarrier
				));

		modelMapper.createTypeMap(BasicBusTripRequestDto.class, BusTrip.class)
				.addMappings(mapper -> mapper.using(datetimeStringToTimestampConverter).map(
						BasicBusTripRequestDto::getDepartureDatetime, BusTrip::setDepartureDateTime
				))
				.addMappings(mapper -> mapper.using(datetimeStringToTimestampConverter).map(
						BasicBusTripRequestDto::getArrivalDatetime, BusTrip::setArrivalDateTime
				))
				.addMappings(mapper -> mapper.using(carrierIdToCarrierConverter).map(
						BasicBusTripRequestDto::getCarrier, BusTrip::setCarrier
				))
				.addMappings(mapper -> mapper.using(fareIdToFareConverter).map(
						BasicBusTripRequestDto::getFare, BusTrip::setFare
				))
				.addMappings(mapper -> mapper.using(busIdToBusConverter).map(
						BasicBusTripRequestDto::getBus, BusTrip::setBus
				))
				.addMappings(mapper -> mapper.using(busPointIdToBusPointConverter).map(
						BasicBusTripRequestDto::getDepartureBusPoint, BusTrip::setDepartureBusPoint
				))
				.addMappings(mapper -> mapper.using(busPointIdToBusPointConverter).map(
						BasicBusTripRequestDto::getArrivalBusPoint, BusTrip::setArrivalBusPoint
				));
	}

	public GetBusTripResponseDto toGetBusTripResponseDto(BusTrip busTrip) {
		return modelMapper.map(busTrip, GetBusTripResponseDto.class);
	}

	public BusTrip fromBasicBusTripRequestDto(BasicBusTripRequestDto dto) {
		return modelMapper.map(dto, BusTrip.class);
	}

	public void fromBasicBusTripRequestDto(BasicBusTripRequestDto dto, BusTrip busTrip) {
		modelMapper.map(dto, busTrip);
	}

}
