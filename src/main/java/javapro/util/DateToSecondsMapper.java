package javapro.util;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateToSecondsMapper {

    Long dateToSecond(Date date) {

        return date.getTime();
    }
}
