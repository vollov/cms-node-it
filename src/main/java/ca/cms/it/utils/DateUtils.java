package ca.cms.it.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	public static String dateToString(LocalDate date, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return date.format(formatter);
	}

	public static LocalDate stringToDate(String dateString) {
		return LocalDate.parse(dateString);
	}

	public static int currentMonth() {
		LocalDate currentDate = LocalDate.now();
		return currentDate.getMonthValue();
	}

	public static int currentYear() {
		LocalDate currentDate = LocalDate.now();
		return currentDate.getYear();
	}

	public static LocalDate currentDate() {
		return LocalDate.now();
	}
}
