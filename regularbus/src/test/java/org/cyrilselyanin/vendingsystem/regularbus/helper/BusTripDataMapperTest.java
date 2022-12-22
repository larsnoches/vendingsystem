package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest()
class BusTripDataMapperTest {

//	@Autowired
//	private BusTripDataMapper busTripDataMapper = new BusTripDataMapper();

	@Test
	void shouldReturnBusTripResponseDto() {

		var timeStamp = Timestamp.valueOf(LocalDateTime.now());
		var d = Date.from(timeStamp.toInstant());

		String formattedDate = new SimpleDateFormat("dd.MM.yyyy").format(timeStamp);
		String formattedTime = new SimpleDateFormat("HH:mm").format(timeStamp);

		String datetimePattern = "dd.MM.yyyy HH:mm";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datetimePattern);

		String datetimeString = String.format("%s %s", formattedDate, formattedTime);
		LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(datetimeString));

		Timestamp timestamp = Timestamp.valueOf(localDateTime);
		assertEquals(datetimeString, timestamp.toString());

//		var formattedDateTimeStamp = Timestamp.valueOf(formattedDate);
//		var formattedTimeTimeStamp = Timestamp.valueOf(formattedTime);

//		var bp = new BusPoint(
//				1L,
//				"buspoint 1",
//				"addr",
//				BusPointType.BUS_STATION,
//				Collections.emptySet(),
//				Collections.emptySet()
//		);
//		BusTrip busTrip = new BusTrip(
//				1L,
//				bp,
//				bp,
//				100,
//				"123",
//				Timestamp.valueOf(LocalDateTime.now()),
//				60,
//				new Fare()
//		);

//		busTripDataMapper.toGetBusTripResponseDto()

	}

}