package com.example.parqueaderouco.utilities;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String DATE_FORMAT_HORA = "yyyy-MM-dd HH:mm:ss";

    public static String DateToStringWithHour (Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_HORA);
        String strDate = dateFormat.format(date);
        return strDate;
    }
}

