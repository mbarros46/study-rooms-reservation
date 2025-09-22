package br.com.fiap.epictaskg.reservation;

import br.com.fiap.epictaskg.room.Room;
import br.com.fiap.epictaskg.room.RoomRepository;
import br.com.fiap.epictaskg.user.User;
import br.com.fiap.epictaskg.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              RoomRepository roomRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Reservation create(Long roomId, Long userId, OffsetDateTime startAt, OffsetDateTime endAt) {
        if (endAt.isBefore(startAt) || endAt.isEqual(startAt)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        Room room = roomRepository.findById(roomId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        if (Boolean.FALSE.equals(room.getActive())) {
            throw new IllegalStateException("Room is not active");
        }

        if (reservationRepository.existsOverlap(roomId, startAt, endAt)) {
            throw new IllegalStateException("Schedule conflict");
        }

        Reservation r = new Reservation();
        r.setRoom(room);
        r.setUser(user);
        r.setStartAt(startAt);
        r.setEndAt(endAt);
        r.setStatus(ReservationStatus.PENDING);
        return reservationRepository.save(r);
    }

    @Transactional
    public Reservation approve(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId).orElseThrow();
        if (r.getStatus() == ReservationStatus.CANCELED) {
            throw new IllegalStateException("Cannot approve a canceled reservation");
        }
        if (reservationRepository.existsOverlap(r.getRoom().getId(), r.getStartAt(), r.getEndAt())) {
            throw new IllegalStateException("Schedule conflict on approve");
        }
        r.setStatus(ReservationStatus.APPROVED);
        return reservationRepository.save(r);
    }

    @Transactional
    public Reservation cancel(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId).orElseThrow();
        r.setStatus(ReservationStatus.CANCELED);
        return reservationRepository.save(r);
    }
}
