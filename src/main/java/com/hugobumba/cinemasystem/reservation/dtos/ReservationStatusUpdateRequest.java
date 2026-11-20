package com.hugobumba.cinemasystem.reservation.dtos;

import com.hugobumba.cinemasystem.reservation.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationStatusUpdateRequest {
    @NotNull(message = "Reservation status is required")
    private ReservationStatus status;
}
