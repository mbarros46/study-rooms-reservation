package br.com.fiap.epictaskg.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
           SELECT COUNT(r) > 0 FROM Reservation r
           WHERE r.room.id = :roomId
             AND r.status IN (br.com.fiap.epictaskg.reservation.ReservationStatus.APPROVED,
                               br.com.fiap.epictaskg.reservation.ReservationStatus.PENDING)
             AND r.startAt < :endAt
             AND r.endAt   > :startAt
           """)
    boolean existsOverlap(@Param("roomId") Long roomId,
                          @Param("startAt") OffsetDateTime startAt,
                          @Param("endAt") OffsetDateTime endAt);

    @Query("""
           SELECT COUNT(r) > 0 FROM Reservation r
           WHERE r.room.id = :roomId
             AND r.id != :reservationId
             AND r.status IN (br.com.fiap.epictaskg.reservation.ReservationStatus.APPROVED,
                               br.com.fiap.epictaskg.reservation.ReservationStatus.PENDING)
             AND r.startAt < :endAt
             AND r.endAt   > :startAt
           """)
    boolean existsOverlapExcluding(@Param("roomId") Long roomId,
                                   @Param("reservationId") Long reservationId,
                                   @Param("startAt") OffsetDateTime startAt,
                                   @Param("endAt") OffsetDateTime endAt);
}
