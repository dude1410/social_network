package javapro.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public  class Time {

    public static long getTime(){
        return LocalDateTime.now()
                .atZone(ZoneId.of("Europe/Moscow"))
                .toInstant()
                .toEpochMilli();
    }
}
