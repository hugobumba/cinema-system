package com.hugobumba.cinemasystem.reservation;

import com.hugobumba.cinemasystem.reservation.dtos.ReservationCreationRequestDTO;
import com.hugobumba.cinemasystem.reservation.dtos.ReservationResponseDTO;
import com.hugobumba.cinemasystem.reservation.dtos.ReservationStatusUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationResponseDTO createReservation(@RequestBody @Valid ReservationCreationRequestDTO request) {
        Reservation reservation = reservationService.createReservation(
                request.getUserId(),
                request.getShowtimeId(),
                request.getSeatIds()
        );
        return toResponse(reservation);
    }

    @GetMapping("/{id}")
    public ReservationResponseDTO getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return toResponse(reservation);
    }

    @GetMapping
    public List<ReservationResponseDTO> listReservations(@RequestParam(required = false) Long userId) {
        List<Reservation> reservations = userId == null
                ? reservationService.listAllReservations()
                : reservationService.listReservationsByUser(userId);
        return reservations.stream().map(this::toResponse).toList();
    }

    @PatchMapping("/{id}/confirm")
    public ReservationResponseDTO confirmReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.confirmReservation(id);
        return toResponse(reservation);
    }

    @PatchMapping("/{id}/cancel")
    public ReservationResponseDTO cancelReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.cancelReservation(id);
        return toResponse(reservation);
    }

    @PatchMapping("/{id}/status")
    public ReservationResponseDTO updateReservationStatus(
            @PathVariable Long id,
            @RequestBody @Valid ReservationStatusUpdateRequest request
    ) {
        return toResponse(reservationService.updateReservationStatus(id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }

    private ReservationResponseDTO toResponse(Reservation reservation) {
        ReservationResponseDTO response = new ReservationResponseDTO();
        response.setId(reservation.getId());
        response.setUserId(reservation.getUser().getId());
        response.setShowtimeId(reservation.getShowtime().getId());
        response.setStatus(reservation.getStatus());
        response.setTotalPrice(reservation.getTotalPrice());
        response.setCreatedAt(reservation.getCreatedAt());
        response.setUpdatedAt(reservation.getUpdatedAt());
        response.setSeatIds(
                reservation.getReservationSeats()
                        .stream()
                        .map(reservationSeat -> reservationSeat.getSeat().getId())
                        .sorted(Comparator.naturalOrder())
                        .toList()
        );
        return response;
    }
}
