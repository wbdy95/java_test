package com.xiaobai.project_data.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    // 用于解析字符串日期
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 将字符串转换为 LocalDate
     */
    public static LocalDate parseLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMAT);
    }

    /**
     * 将字符串转换为 java.util.Date（兼容旧代码）
     */
    public static Date parseDate(String dateStr) {
        return java.sql.Date.valueOf(parseLocalDate(dateStr));
    }

    /**
     * 增加天数
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }


    // 将 java.util.Date 转换为 LocalDate
    public static LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public static String formatDateToString(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return DATE_FORMAT.format(localDate);
    }

    // 组合使用：Date -> LocalDate -> String
    public static String format(Date date) {
        return formatDateToString(convertToLocalDate(date));
    }


}
