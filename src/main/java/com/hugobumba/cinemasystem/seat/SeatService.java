package com.hugobumba.cinemasystem.seat;

import com.hugobumba.cinemasystem.room.Room;
import com.hugobumba.cinemasystem.room.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SeatService {

    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;

    public SeatService(SeatRepository seatRepository, RoomRepository roomRepository) {
        this.seatRepository = seatRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Seat createSeat(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("Seat is required");
        }
        if (seat.getId() == null) {
            throw new IllegalArgumentException("Seat id is required");
        }
        if (seat.getRoom() == null || seat.getRoom().getId() == null) {
            throw new IllegalArgumentException("Room is required");
        }
        if (seat.getSeatRow() == null) {
            throw new IllegalArgumentException("Seat row is required");
        }
        if (seat.getSeatNumber() == null || seat.getSeatNumber().isBlank()) {
            throw new IllegalArgumentException("Seat number is required");
        }
        if (seatRepository.existsById(seat.getId())) {
            throw new IllegalArgumentException("Seat already exists for id: " + seat.getId());
        }
        Long roomId = seat.getRoom().getId();

        Optional<Seat> existingSeat = seatRepository.findByRoomIdAndSeatRowAndSeatNumber(
                roomId,
                seat.getSeatRow(),
                seat.getSeatNumber()
        );
        if (existingSeat.isPresent()) {
            throw new IllegalArgumentException("Seat already exists in room for row/number");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("Room not found: " + roomId));
        seat.setRoom(room);
        return seatRepository.save(seat);
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Seat not found: " + id));
    }

    public List<Seat> listAllSeats() {
        return seatRepository.findAll();
    }

    public List<Seat> listSeatsByRoom(Long roomId) {
        return seatRepository.findByRoomId(roomId);
    }

    @Transactional
    public Seat updateSeat(Seat seat) {
        if (seat == null || seat.getId() == null) {
            throw new IllegalArgumentException("Seat id is required");
        }
        if (seat.getRoom() == null || seat.getRoom().getId() == null) {
            throw new IllegalArgumentException("Room is required");
        }
        Seat existingSeat = getSeatById(seat.getId());
        Long roomId = seat.getRoom().getId();
        if (seat.getSeatRow() == null) {
            throw new IllegalArgumentException("Seat row is required");
        }
        if (seat.getSeatNumber() == null || seat.getSeatNumber().isBlank()) {
            throw new IllegalArgumentException("Seat number is required");
        }
        Optional<Seat> duplicatedSeat = seatRepository.findByRoomIdAndSeatRowAndSeatNumber(
                roomId,
                seat.getSeatRow(),
                seat.getSeatNumber()
        );
        if (duplicatedSeat.isPresent() && !duplicatedSeat.get().getId().equals(seat.getId())) {
            throw new IllegalArgumentException("Seat already exists in room for row/number");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("Room not found: " + roomId));
        existingSeat.setRoom(room);

        existingSeat.setSeatRow(seat.getSeatRow());
        existingSeat.setSeatNumber(seat.getSeatNumber());
        return seatRepository.save(existingSeat);
    }

    @Transactional
    public void deleteSeat(Long id) {
        Seat seat = getSeatById(id);
        seatRepository.delete(seat);
    }
}
