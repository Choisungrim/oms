package com.humuson.oms.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.humuson.oms.exception.CustomException;
import com.humuson.oms.util.constants.SystemConstants;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
        dateFormat.setTimeZone(TimeZone.getDefault()); // 시간대 설정

        Date currentDate = new Date();
        // 현재 시간 가져오기
        LocalDateTime currentDateTime = LocalDateTime.now();


        // 날짜 형식인지 확인
        if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            // 날짜만 있는 경우
            String currentTimeStr = new SimpleDateFormat(TIME_FORMAT).format(currentDateTime);
            dateStr = dateStr + " " + currentTimeStr; // 현재 날짜와 결합
            try {
                dateFormat.parse(dateStr);
            } catch (ParseException e) {
                throw new CustomException(dateStr, "deserialize");
            }
        } else if (dateStr.matches("\\d{2}:\\d{2}:\\d{2}")) {
            // 시간만 있는 경우
            String currentDateStr = new SimpleDateFormat(DATE_ONLY_FORMAT).format(currentDate);
            dateStr = currentDateStr + " " + dateStr; // 현재 날짜와 결합
            try {
                dateFormat.parse(dateStr);
            } catch (ParseException e) {
                throw new CustomException(dateStr, "deserialize");
            }
        } else {
            throw new CustomException(dateStr, "deserialize");
        }

        if (!dateStr.contains(".")) {
            dateStr += ".000";
        }

        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new CustomException(dateStr, "deserialize");
        }
    }

    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setLenient(false); // 엄격한 날짜 형식 검사를 위해 설정
        try {
            dateFormat.parse(dateStr);
            return true; // 유효한 날짜
        } catch (Exception e) {
            throw new CustomException(dateStr, "isValidDate");
        }
    }
}
