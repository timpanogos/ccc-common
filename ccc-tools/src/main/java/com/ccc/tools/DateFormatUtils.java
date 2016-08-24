/*
**  Copyright (c) 2016, Chad Adams.
**
**  This program is free software: you can redistribute it and/or modify
**  it under the terms of the GNU Lesser General Public License as 
**  published by the Free Software Foundation, either version 3 of the 
**  License, or any later version.
**
**  This program is distributed in the hope that it will be useful,
**  but WITHOUT ANY WARRANTY; without even the implied warranty of
**  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
**  GNU General Public License for more details.

**  You should have received copies of the GNU GPLv3 and GNU LGPLv3
**  licenses along with this program.  If not, see http://www.gnu.org/licenses
*/
package com.ccc.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for formatting dates and times.
 */
public final class DateFormatUtils {

	private static final String utcFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	private DateFormatUtils() {
		// prevent construction
	}

	/**
	 * Format a Date into a String, according to the format specified by RFC 3339, and conforming to the ISO 8601 complete date and time extended format in UTC.
	 * @param date The date to be formatted. Must not be null.
	 * @return String representing the formatted Date.
	 */
	public static String formatUTC(Date date) {
	    return org.apache.commons.lang3.time.DateFormatUtils.formatUTC(date, utcFormat);
	}
	
	/**
	 * Format a millisecond UTC time into a String, according to the format specified by RFC 3339, and conforming to the ISO 8601 complete date and time extended format in UTC.
	 * @param date The millisecond UTC time to be formatted. Must not be null.
	 * @return String representing the formatted Date.
	 */
	public static String formatUTC(long date) {
	    return org.apache.commons.lang3.time.DateFormatUtils.formatUTC(date, utcFormat);
	}
	
	/**
	 * Parse a Date from a String, according to the format specified by RFC 3339, and conforming to the ISO 8601 complete date and time extended format in UTC.
	 * @param date The string representing date to be formatted. Assumes the same format as produced by @link{formatUTC}. Must not be null.
	 * @return Date The parsed Date.
	 * @exception ParseException if the string is in an unrecognizable format.
	 */
	public static Date parseUTC(String date) throws ParseException {
		// Since SimpleDateFormat is not thread-safe, we instantiate a new one for each conversion.
		SimpleDateFormat df = new SimpleDateFormat(utcFormat);
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.parse(date);
	}

	/**
	 * Retrieve the standard UTC Format as specified by RFC 3339.
	 * @return The standard UTC Format as specified by RFC 3339.
	 */
	public static String getUTCFormat() {
		return utcFormat;
	}
}
