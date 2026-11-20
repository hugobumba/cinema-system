package com.hugobumba.cinemasystem.reservation;

import com.hugobumba.cinemasystem.seat.Seat;
import com.hugobumba.cinemasystem.seat.SeatRepository;
import com.hugobumba.cinemasystem.showtime.Showtime;
import com.hugobumba.cinemasystem.showtime.ShowtimeRepository;
import com.hugobumba.cinemasystem.user.User;
import com.hugobumba.cinemasystem.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Reservation createReservation(Long userId, Long showtimeId, List<Long> seatIds, Double discount) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }
        if (showtimeId == null) {
            throw new IllegalArgumentException("Showtime id is required");
        }
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("At least one seat is required");
        }
        if (discount == null || discount < 0 || discount > 1) {
            throw new IllegalArgumentException("Discount must be between 0 and 1");
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
        reservation.setDiscount(discount);
        reservation.setTotalPrice(showtime.getPrice() * uniqueSeatIds.size() * (1 - discount));
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
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled reservation cannot be confirmed");
        }
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
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
}
