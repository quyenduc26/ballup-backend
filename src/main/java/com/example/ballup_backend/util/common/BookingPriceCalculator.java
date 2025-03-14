package com.example.ballup_backend.util.common;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class BookingPriceCalculator {

    private static final LocalTime DAY_START = LocalTime.of(6, 0);
    private static final LocalTime NIGHT_START = LocalTime.of(18, 0);

   public Long calculateBookingPrice(Timestamp fromTimestamp, Timestamp toTimestamp, Integer dayRate, Integer nightRate) {
    LocalDateTime fromTime = fromTimestamp.toLocalDateTime();
    LocalDateTime toTime = toTimestamp.toLocalDateTime();

    double totalCost = 0.0;

    while (fromTime.isBefore(toTime)) {
        LocalTime currentTime = fromTime.toLocalTime();

        // Xác định mức giá theo thời điểm
        double rate = (currentTime.isAfter(DAY_START) && currentTime.isBefore(NIGHT_START)) ? dayRate : nightRate;

        // Kiểm tra thời gian còn lại để tính giá chính xác cho phút lẻ
        long minutesRemaining = ChronoUnit.MINUTES.between(fromTime, toTime);
        long minutesToCharge = Math.min(60, minutesRemaining); // Không vượt quá khoảng thời gian thực tế còn lại

        totalCost += (rate / 60.0) * minutesToCharge; // Tính giá dựa trên số phút thực tế
        fromTime = fromTime.plusMinutes(minutesToCharge); // Chuyển sang phút tiếp theo
    }

    // Làm tròn đến hàng nghìn gần nhất
    return Math.round(totalCost / 1000.0) * 1000L;
}

}


