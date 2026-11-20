package com.hugobumba.cinemasystem.reservation.dtos;

import com.hugobumba.cinemasystem.reservation.ReservationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ReservationResponseDTO {
    private Long id;
    private Long userId;
    private Long showtimeId;
    private ReservationStatus status;
    private BigDecimal totalPrice;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    private java.util.List<Long> seatIds;
}
