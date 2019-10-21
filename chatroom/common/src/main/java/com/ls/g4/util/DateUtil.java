package com.ls.g4.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateUtil {
  private DateUtil() {
  }

  public static final String FORMATTER = "yyyy/MM/dd HH:mm";

  public static Date string2Date(String str) {
    DateFormat format = new SimpleDateFormat(FORMATTER);
    Date date = null;
    try {
      date = format.parse(str);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  public static LocalDateTime string2LocalDateTime(String str) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern(FORMATTER);
    LocalDateTime time = LocalDateTime.parse(str, df);
    return time;
  }

  public static String localDateTime2String(LocalDateTime time) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern(FORMATTER);
    return df.format(time);
  }

  public static String long2DateString(long time) {
    Date date = new Date(time);
    SimpleDateFormat format = new SimpleDateFormat(FORMATTER);
    return format.format(date);
  }

}
