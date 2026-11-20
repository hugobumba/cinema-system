package com.hugobumba.cinemasystem.reservation;

import com.hugobumba.cinemasystem.seat.Seat;
import com.hugobumba.cinemasystem.seat.SeatRepository;
import com.hugobumba.cinemasystem.showtime.Showtime;
import com.hugobumba.cinemasystem.showtime.ShowtimeRepository;
import com.hugobumba.cinemasystem.user.User;
import com.hugobumba.cinemasystem.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationSeatRepository reservationSeatRepository,
            UserRepository userRepository,
            ShowtimeRepository showtimeRepository,
            SeatRepository seatRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationSeatRepository = reservationSeatRepository;
        this.userRepository = userRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found: " + id));
    }

    public List<Reservation> listAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> listReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Transactional
    public Reservation createReservation(Long userId, Long showtimeId, List<Long> seatIds) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }
        if (showtimeId == null) {
            throw new IllegalArgumentException("Showtime id is required");
        }
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("At least one seat is required");
        }
        Set<Long> uniqueSeatIds = new LinkedHashSet<>(seatIds);
        if (uniqueSeatIds.size() != seatIds.size()) {
            throw new IllegalArgumentException("Duplicate seat ids are not allowed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new NoSuchElementException("Showtime not found: " + showtimeId));

        List<Seat> seats = seatRepository.findAllById(uniqueSeatIds);
        if (seats.size() != uniqueSeatIds.size()) {
            throw new IllegalArgumentException("One or more seats do not exist");
        }

        Long showtimeRoomId = showtime.getRoom().getId();
        boolean invalidSeatRoom = seats.stream().anyMatch(seat -> !seat.getRoom().getId().equals(showtimeRoomId));
        if (invalidSeatRoom) {
            throw new IllegalArgumentException("All seats must belong to the showtime room");
        }

        long alreadyReserved = reservationSeatRepository.countByIdShowtimeIdAndIdSeatIdIn(showtimeId, uniqueSeatIds);
        if (alreadyReserved > 0) {
            throw new IllegalStateException("One or more seats are already reserved for this showtime");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setShowtime(showtime);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        reservation.setTotalPrice(showtime.getPrice().multiply(BigDecimal.valueOf(uniqueSeatIds.size())));
        Reservation savedReservation = reservationRepository.save(reservation);

        Set<ReservationSeat> reservationSeats = new LinkedHashSet<>();
        for (Seat seat : seats) {
            ReservationSeat reservationSeat = new ReservationSeat();
            reservationSeat.setId(new ReservationSeatId(savedReservation.getId(), seat.getId(), showtimeId));
            reservationSeat.setReservation(savedReservation);
            reservationSeat.setSeat(seat);
            reservationSeat.setShowtime(showtime);
            reservationSeats.add(reservationSeat);
        }
        reservationSeatRepository.saveAll(reservationSeats);
        savedReservation.setReservationSeats(reservationSeats);
        return savedReservation;
    }

    @Transactional
    public Reservation confirmReservation(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        expireIfPendingAndStarted(reservation);
        if (!(reservation.getStatus() == ReservationStatus.PENDING)) {
            throw new IllegalStateException("Only pending reservations can be confirmed");
        }
        if (reservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot confirm reservation: showtime has already started");
        }
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        expireIfPendingAndStarted(reservation);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel reservation: already cancelled");
        }
        if (reservation.getStatus() == ReservationStatus.EXPIRED) {
            throw new IllegalStateException("Cannot cancel reservation: already expired");
        }
        if(reservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot cancel reservation: showtime has already started");
        }
        for (ReservationSeat reservationSeat : reservation.getReservationSeats()) {
            Long showtimeId = reservationSeat.getShowtime().getId();
            Long seatId = reservationSeat.getSeat().getId();
            boolean isReserved = reservationSeatRepository.existsByIdShowtimeIdAndSeatId(showtimeId, seatId);
            if (!isReserved) {
                throw new IllegalStateException("Cannot cancel reservation: one or more seats are not reserved");
            }
            reservationSeatRepository.deleteByIdShowtimeIdAndSeatId(showtimeId, seatId);
        }

        reservation.setReservationSeats(new HashSet<>());
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        reservationSeatRepository.deleteByReservationId(reservationId);
        reservationRepository.delete(reservation);
    }

    private void expireIfPendingAndStarted(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.PENDING
                && reservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservation.setUpdatedAt(LocalDateTime.now());
            reservationRepository.save(reservation);
        }
    }
}
