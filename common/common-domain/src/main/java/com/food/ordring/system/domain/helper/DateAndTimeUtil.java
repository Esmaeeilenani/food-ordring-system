package com.food.ordring.system.domain.helper;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateAndTimeUtil {

    private final static String UTC = "UTC";

    private  DateAndTimeUtil(){

    }



    public static ZonedDateTime zonedDateTimeUTCNow() {
        return ZonedDateTime.now(ZoneId.of(UTC));
    }
}
