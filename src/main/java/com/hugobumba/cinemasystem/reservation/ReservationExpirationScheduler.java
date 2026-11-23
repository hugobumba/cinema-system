package com.hugobumba.cinemasystem.reservation;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationExpirationScheduler {

    private final ReservationService reservationService;
    private final ReservationExpirationProperties reservationExpirationProperties;

    public ReservationExpirationScheduler(
            ReservationService reservationService,
            ReservationExpirationProperties reservationExpirationProperties
    ) {
        this.reservationService = reservationService;
        this.reservationExpirationProperties = reservationExpirationProperties;
    }

    @Scheduled(fixedDelayString = "${reservation.expiration.scheduler-delay-ms:60000}")
    public void expirePendingReservations() {
        long expirationMinutes = reservationExpirationProperties.getMinutes();
        if (expirationMinutes <= 0) {
            return;
        }

        LocalDateTime cutoffDateTime = LocalDateTime.now().minusMinutes(expirationMinutes);
        reservationService.expirePendingReservationsBefore(cutoffDateTime);
    }
}
