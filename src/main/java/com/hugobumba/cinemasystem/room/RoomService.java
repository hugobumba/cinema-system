package com.hugobumba.cinemasystem.room;

import com.hugobumba.cinemasystem.cinema.Cinema;
import com.hugobumba.cinemasystem.cinema.CinemaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    public RoomService(RoomRepository roomRepository, CinemaRepository cinemaRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @Transactional
    public Room createRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room is required");
        }
        if (room.getId() == null) {
            throw new IllegalArgumentException("Room id is required");
        }
        if (room.getNumber() == null || room.getNumber().isBlank()) {
            throw new IllegalArgumentException("Room number is required");
        }
        if (room.getCinema() == null || room.getCinema().getId() == null) {
            throw new IllegalArgumentException("Cinema is required");
        }
        Cinema cinema = cinemaRepository.findById(room.getCinema().getId())
                .orElseThrow(() -> new NoSuchElementException("Cinema not found: " + room.getCinema().getId()));
        room.setCinema(cinema);
        return roomRepository.save(room);
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room not found: " + id));
    }

    public List<Room> listAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> listRoomsByCinemaId(Long cinemaId) {
        return roomRepository.findByCinemaId(cinemaId);
    }

    @Transactional
    public Room updateRoom(Room room) {
        if (room == null || room.getId() == null) {
            throw new IllegalArgumentException("Room id is required");
        }
        Room existingRoom = getRoomById(room.getId());
        if (room.getNumber() == null || room.getNumber().isBlank()) {
            throw new IllegalArgumentException("Room number is required");
        }
        if (room.getCinema() == null || room.getCinema().getId() == null) {
            throw new IllegalArgumentException("Cinema is required");
        }
        Cinema cinema = cinemaRepository.findById(room.getCinema().getId())
                .orElseThrow(() -> new NoSuchElementException("Cinema not found: " + room.getCinema().getId()));

        existingRoom.setNumber(room.getNumber());
        existingRoom.setCinema(cinema);
        return roomRepository.save(existingRoom);
    }

    @Transactional
    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }
}
