package com.max.log.agent.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String dateFormat(Date dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = format.format(dateTime);
        return formatDateTime;
    }

}
