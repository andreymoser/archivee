/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package biz.bidi.archivee.commons.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import biz.bidi.archivee.commons.exceptions.ArchiveeException;
import biz.bidi.archivee.commons.properties.ArchiveeProperties;
import biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 22, 2012
 */
public class ArchiveeDateUtils implements IArchiveePropertiesLoader {
	
	private static final ArchiveeDateUtils instance = new ArchiveeDateUtils();
	
	/**
	 * The common dateLocale among archivee components
	 */
	private String dateLocale;
	/**
	 * The common simple date format among archivee components
	 */
	private String simpleDateFormat;
	
	public ArchiveeDateUtils() {
		try {
			loadProperties("");
		} catch (ArchiveeException e) {
			ArchiveeException.log(e, "Unable to load common date properties", this);
		}
	}
	
	/**
	 * @param dateString
	 * @return
	 */
	public static Date convertToDate(String dateString) throws ArchiveeException {
		Date date = null;
		
		try {
			DateFormat dateFormat = new SimpleDateFormat(instance.getSimpleDateFormat(),new Locale(instance.getDateLocale()));
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			ArchiveeException.log(e, "Error on string to date conversion: " + dateString, dateString,instance);
		}
		
		return date;
	}
	
	/**
	 * Converts the date using the default date format used among archivee components
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Date date) {
		String dateStr = "";
		
		DateFormat dateFormat = new SimpleDateFormat(instance.getSimpleDateFormat(),new Locale(instance.getDateLocale()));
		dateStr = dateFormat.format(date);
		
		return dateStr;
	}
	
	/**
	 * Converts the simple date format to regex format
	 * @param simpleDateFormat
	 * @return
	 */
	public static String convertSimpleDateFormatToRegex(String simpleDateFormat) {
		String str = new String(simpleDateFormat);
		
		str = str.replaceAll("y", "0");
		str = str.replaceAll("MMM", "a");
		str = str.replaceAll("MM", "0");
		str = str.replaceAll("d", "0");
		
		str = str.replaceAll("h", "0");
		str = str.replaceAll("m", "0");
		str = str.replaceAll("s", "0");
		str = str.replaceAll("S", "0");
		
		return ArchiveePatternUtils.convertSimpleRegexToRegex(ArchiveePatternUtils.convertToSimpleRegex(str));
	}
	
	/**
	 * Converts a valid string date to standard date, otherwise returns null
	 * @param str
	 * @param simpleDateFormat
	 * @return
	 * @throws ArchiveeException
	 */
	public static Date stringToDate(String str, String simpleDateFormat, String locale) throws ArchiveeException {
		Date date = null;
		
		try {
			DateFormat dateFormat = new SimpleDateFormat(simpleDateFormat,new Locale(locale));
			date = dateFormat.parse(str);
		} catch (ParseException e) {
			date = null;
		}
		
		return date;
	}
	
	/**
	 * Find the first valid string date for the given simple date format, for any exception it will return empty
	 * @param str
	 * @param simpleDateFormat
	 * @return
	 * @throws ArchiveeException
	 */
	public static String findDate(String str, String simpleDateFormat, String locale) throws ArchiveeException {
		String dateStr = "";
		
		String[] values = ArchiveePatternUtils.getRegexValues(str, convertSimpleDateFormatToRegex(simpleDateFormat));
		
		if(values.length > 0) {
			Date date = stringToDate(values[0], simpleDateFormat, locale);
			/**
			 * if date found is valid
			 */
			if(date != null) {
				dateStr = values[0];
			}
		}
		
		return dateStr;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see biz.bidi.archivee.commons.properties.IArchiveePropertiesLoader#loadProperties(java.lang.String)
	 */
	@Override
	public void loadProperties(String prefixKey) throws ArchiveeException {
		ArchiveeProperties.loadProperties(this);
	}

	/**
	 * @return the dateLocale
	 */
	public String getDateLocale() {
		return dateLocale;
	}

	/**
	 * @param dateLocale the dateLocale to set
	 */
	public void setDateLocale(String dateLocale) {
		this.dateLocale = dateLocale;
	}

	/**
	 * @return the simpleDateFormat
	 */
	public String getSimpleDateFormat() {
		return simpleDateFormat;
	}

	/**
	 * @param simpleDateFormat the simpleDateFormat to set
	 */
	public void setSimpleDateFormat(String simpleDateFormat) {
		this.simpleDateFormat = simpleDateFormat;
	}

	
}
