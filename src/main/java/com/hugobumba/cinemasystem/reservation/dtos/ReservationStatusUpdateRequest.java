package com.hugobumba.cinemasystem.reservation.dtos;

import com.hugobumba.cinemasystem.reservation.ReservationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationStatusUpdateRequest {
    private ReservationStatus status;
}
