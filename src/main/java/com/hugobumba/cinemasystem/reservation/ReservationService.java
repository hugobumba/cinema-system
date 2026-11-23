package com.hugobumba.cinemasystem.reservation;

import com.hugobumba.cinemasystem.seat.Seat;
import com.hugobumba.cinemasystem.seat.SeatRepository;
import com.hugobumba.cinemasystem.showtime.Showtime;
import com.hugobumba.cinemasystem.showtime.ShowtimeRepository;
import com.hugobumba.cinemasystem.user.User;
import com.hugobumba.cinemasystem.user.UserRepository;
import com.hugobumba.cinemasystem.user.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found: " + id));
        enforceReservationAccess(reservation);
        return reservation;
    }

    public List<Reservation> listAllReservations() {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser.getRole() == UserRole.CUSTOMER) {
            return reservationRepository.findByUserId(currentUser.getId());
        }
        return reservationRepository.findAll();
    }

    public List<Reservation> listReservationsByUser(Long userId) {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser.getRole() == UserRole.CUSTOMER && !currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("Customers can only access their own reservations");
        }
        return reservationRepository.findByUserId(userId);
    }

    @Transactional
    public Reservation createReservation(Long showtimeId, List<Long> seatIds) {
        User currentUser = getCurrentAuthenticatedUser();
        return createReservationForUser(currentUser.getId(), showtimeId, seatIds);
    }

    @Transactional
    public Reservation createReservationForUser(Long userId, Long showtimeId, List<Long> seatIds) {
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
        enforceUserCreationAccess(user);
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
        return updateReservationStatus(reservationId, ReservationStatus.CONFIRMED);
    }

    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        return updateReservationStatus(reservationId, ReservationStatus.CANCELLED);
    }

    @Transactional
    public Reservation updateReservationStatus(Long reservationId, ReservationStatus targetStatus) {
        if (targetStatus == null) {
            throw new IllegalArgumentException("Reservation target status is required");
        }
        if (targetStatus == ReservationStatus.EXPIRED) {
            throw new IllegalStateException("Reservations can only be expired by the expiration flow");
        }

        return applyReservationStatusTransition(reservationId, targetStatus, false);
    }

    @Transactional
    public Reservation expireReservation(Long reservationId) {
        return applyReservationStatusTransition(reservationId, ReservationStatus.EXPIRED, true);
    }

    private Reservation applyReservationStatusTransition(
            Long reservationId,
            ReservationStatus targetStatus,
            boolean allowSystemExpiration
    ) {
        Reservation reservation = getReservationById(reservationId);
        ReservationStatus currentStatus = reservation.getStatus();
        if (!isTransitionAllowed(currentStatus, targetStatus, allowSystemExpiration)) {
            throw new IllegalStateException("Invalid reservation transition: " + currentStatus + " -> " + targetStatus);
        }

        if (targetStatus == ReservationStatus.CANCELLED || targetStatus == ReservationStatus.EXPIRED) {
            releaseReservedSeats(reservation, targetStatus);
        }

        reservation.setStatus(targetStatus);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        reservationSeatRepository.deleteByReservationId(reservationId);
        reservationRepository.delete(reservation);
    }

    private void enforceReservationAccess(Reservation reservation) {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser.getRole() == UserRole.CUSTOMER
                && !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Customers can only access their own reservations");
        }
    }

    private void enforceUserCreationAccess(User targetUser) {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser.getRole() == UserRole.CUSTOMER
                && !targetUser.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Customers can only create reservations for themselves");
        }
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Authentication is required");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Authenticated user not found: " + email));
    }

    private void releaseReservedSeats(Reservation reservation, ReservationStatus targetStatus) {
        for (ReservationSeat reservationSeat : reservation.getReservationSeats()) {
            Long showtimeId = reservationSeat.getShowtime().getId();
            Long seatId = reservationSeat.getSeat().getId();
            boolean isReserved = reservationSeatRepository.existsByIdShowtimeIdAndSeatId(showtimeId, seatId);
            if (!isReserved) {
                throw new IllegalStateException(
                        "Cannot set reservation to " + targetStatus + ": one or more seats are not reserved"
                );
            }
            reservationSeatRepository.deleteByIdShowtimeIdAndSeatId(showtimeId, seatId);
        }
        reservation.setReservationSeats(new HashSet<>());
    }

    private boolean isTransitionAllowed(ReservationStatus from, ReservationStatus to, boolean allowSystemExpiration) {
        if (from == to) {
            return false;
        }
        return switch (from) {
            case PENDING -> to == ReservationStatus.CONFIRMED
                    || to == ReservationStatus.CANCELLED
                    || (allowSystemExpiration && to == ReservationStatus.EXPIRED);
            case CONFIRMED -> to == ReservationStatus.CANCELLED;
            case CANCELLED, EXPIRED -> false;
        };
    }
}
