package com.humuson.oms.util.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.humuson.oms.exception.CustomException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateDeserializer extends JsonDeserializer<Date> {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss.SSS";

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String dateStr = jsonParser.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getDefault());



        try {
            // 날짜 형식 확인
            if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                // 날짜만 있는 경우
                LocalDateTime currentDateTime = LocalDateTime.now();
                String currentTimeStr = DateTimeFormatter.ofPattern(TIME_FORMAT).format(currentDateTime);
                dateStr += " " + currentTimeStr; // 현재 시간 추가
            } else if (dateStr.matches("\\d{2}:\\d{2}:\\d{2}")) {
                // 시간만 있는 경우
                String currentDateStr = new SimpleDateFormat(DATE_ONLY_FORMAT).format(new Date());
                dateStr = currentDateStr + " " + dateStr; // 현재 날짜 추가
            }

            if (dateStr.endsWith("Z")) {
                dateStr = dateStr.replace("Z", "+0000"); // Z를 +0000으로 변환
            }

            if (dateStr.contains("T")) {
                dateStr = dateStr.replace("T", " "); // Z를 +0000으로 변환
            }
            // 밀리초가 포함되어 있지 않은 경우 추가
            if (!dateStr.contains(".")) {
                dateStr += ".000"; // 밀리초 추가
            }

            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new CustomException("Failed to parse date: " + dateStr, "deserialize");
        }
    }
}
