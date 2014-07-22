package org.trii.tinyspring.utils;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: June 22, 2014
 * Time: 23:17
 * <p/>
 * DateTimeUtils using JodaTime library
 */
public class DateTimeUtils {

	static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	static DateFormat hourMinuteFormatter = new SimpleDateFormat("HH:mm");

	/**
	 * formats date to HH:mm
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toHHmm(Date date) {

		return hourMinuteFormatter.format(date);
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static String toDateString(Date date) {

		return dateFormatter.format(date);
	}

	public static Timestamp toTimestamp(String dateString) {

		try {
			return new Timestamp(dateFormatter.parse(dateString).getTime());
		} catch(ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * returns a timestamp representing the given date at 00:00:00.<br>
	 * A offset equals to zero means same day as the given time. A offset less than zero
	 * means [offset] days before the given time. a offset greater than zero means
	 * [offset] days after the given time.
	 *
	 * @param time
	 * 		a certain time
	 * @param offset
	 * 		offset in days.
	 *
	 * @return
	 */
	public static Date beginOfDay(Date time, int offset) {

		DateTime jodaTime = new DateTime(time.getTime());
		jodaTime = jodaTime
				.hourOfDay().withMinimumValue()
				.minuteOfHour().withMinimumValue()
				.secondOfMinute().withMinimumValue();

		jodaTime = jodaTime.plusDays(offset);
		return new Date(jodaTime.getMillis());
	}

	/**
	 * returns a timestamp representing the given date at 00:00:00
	 *
	 * @param time
	 * 		a certain time
	 *
	 * @return the specific date
	 */
	public static Date beginOfDay(Date time) {

		DateTime jodaTime = new DateTime(time.getTime());
		jodaTime = jodaTime
				.hourOfDay().withMinimumValue()
				.minuteOfHour().withMinimumValue()
				.secondOfMinute().withMinimumValue();

		jodaTime = jodaTime.plusDays(0);
		return new Date(jodaTime.getMillis());
	}

	/**
	 * returns a date representing today at 00:00:00
	 *
	 * @return the specific date
	 */
	public static Date beginOfDay() {

		DateTime jodaTime = new DateTime(System.currentTimeMillis());
		jodaTime = jodaTime
				.hourOfDay().withMinimumValue()
				.minuteOfHour().withMinimumValue()
				.secondOfMinute().withMinimumValue();

		jodaTime = jodaTime.plusDays(0);
		return new Date(jodaTime.getMillis());
	}

	public static Date endOfDay(Date time, int offset) {

		DateTime jodaTime = new DateTime(time.getTime());
		jodaTime = jodaTime
				.hourOfDay().withMaximumValue()
				.minuteOfHour().withMaximumValue()
				.secondOfMinute().withMaximumValue();
		jodaTime = jodaTime.plusDays(offset);
		return new Date(jodaTime.getMillis());
	}

	public static Date beginOfMonth(int year, int month, int offset) {

		return new MutableDateTime()
				.year().set(year)
				.monthOfYear().set(month)
				.toDateTime()
				.dayOfMonth().withMinimumValue()
				.hourOfDay().withMinimumValue()
				.minuteOfHour().withMinimumValue()
				.secondOfMinute().withMinimumValue()
				.millisOfSecond().withMinimumValue()
				.plusMonths(offset)
				.toDate();
	}

	public static Date endOfMonth(int year, int month, int offset) {

		return new MutableDateTime()
				.year().set(year)
				.monthOfYear().set(month)
				.toDateTime()
				.dayOfMonth().withMaximumValue()
				.hourOfDay().withMaximumValue()
				.minuteOfHour().withMaximumValue()
				.secondOfMinute().withMaximumValue()
				.millisOfSecond().withMaximumValue()
				.plusMonths(offset)
				.toDate();
	}

	public static Timestamp now() {

		return new Timestamp(System.currentTimeMillis());
	}
}
