package com.kr.realworldspringboot.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalDateUtcParser {

    public ZonedDateTime localDateTimeParseUTC(LocalDateTime localDateTime){
        ZonedDateTime ldtZoned = localDateTime.atZone(ZoneId.systemDefault());
        return ldtZoned.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
    }

}
