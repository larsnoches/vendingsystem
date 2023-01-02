package org.cyrilselyanin.vendingsystem.regularbus.helper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

	private static final String datetimePattern = "dd.MM.yyyy HH:mm";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datetimePattern);

	private TimeUtil() {
		//
	}

	public static Timestamp createTimestamp(String datetime) {
		LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(datetime));
		return Timestamp.valueOf(localDateTime);
	}

}
