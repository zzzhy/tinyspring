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
 * <p>
 * DateTimeUtils using JodaTime library
 *
 * @author tian
 * @version $Id: $Id
 */
public class DateTimeUtils {

	static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	static DateFormat hourMinuteFormatter = new SimpleDateFormat("HH:mm");

	/**
	 * formats date to HH:mm
	 *
	 * @param date
	 * 		a {@link java.util.Date} object.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String toHHmm(Date date) {

		return hourMinuteFormatter.format(date);
	}

	/**
	 * <p>toDateString.</p>
	 *
	 * @param date
	 * 		a {@link java.util.Date} object.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String toDateString(Date date) {

		return dateFormatter.format(date);
	}

	/**
	 * <p>toTimestamp.</p>
	 *
	 * @param dateString
	 * 		a {@link java.lang.String} object.
	 *
	 * @return a {@link java.sql.Timestamp} object.
	 */
	public static Timestamp toTimestamp(String dateString) {

		try {
			return new Timestamp(dateFormatter.parse(dateString).getTime());
		} catch(ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * a offset equals to zero means same day as the given time.a offset less than zero
	 * means [offset] days before the given time. a offset greater than zero means
	 * [offset] days after the given time.<br>
	 *
	 * @param time
	 * 		a {@link java.util.Date} object.
	 * @param offset
	 * 		offset in days.
	 *
	 * @return a {@link java.util.Date} object.
	 */
	public static Date toStartOfDay(Date time, int offset) {

		DateTime jodaTime = new DateTime(time.getTime());
		jodaTime = jodaTime
				.hourOfDay().withMinimumValue()
				.minuteOfHour().withMinimumValue()
				.secondOfMinute().withMinimumValue();

		jodaTime = jodaTime.plusDays(offset);
		return new Date(jodaTime.getMillis());
	}

	/**
	 * <p>toEndOfDay.</p>
	 *
	 * @param time
	 * 		a {@link java.util.Date} object.
	 * @param offset
	 * 		a int.
	 *
	 * @return a {@link java.util.Date} object.
	 */
	public static Date toEndOfDay(Date time, int offset) {

		DateTime jodaTime = new DateTime(time.getTime());
		jodaTime = jodaTime
				.hourOfDay().withMaximumValue()
				.minuteOfHour().withMaximumValue()
				.secondOfMinute().withMaximumValue();
		jodaTime = jodaTime.plusDays(offset);
		return new Date(jodaTime.getMillis());
	}

	/**
	 * <p>toStartOfMonth.</p>
	 *
	 * @param year
	 * 		a int.
	 * @param month
	 * 		a int.
	 * @param offset
	 * 		a int.
	 *
	 * @return a {@link java.util.Date} object.
	 */
	public static Date toStartOfMonth(int year, int month, int offset) {

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

	/**
	 * <p>toEndOfMonth.</p>
	 *
	 * @param year
	 * 		a int.
	 * @param month
	 * 		a int.
	 * @param offset
	 * 		a int.
	 *
	 * @return a {@link java.util.Date} object.
	 */
	public static Date toEndOfMonth(int year, int month, int offset) {

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

	/**
	 * <p>now.</p>
	 *
	 * @return a {@link java.sql.Timestamp} object.
	 */
	public static Timestamp now() {

		return new Timestamp(System.currentTimeMillis());
	}
}
