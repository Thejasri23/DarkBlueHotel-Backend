package com.darkblue.DarkBlueHotel.repo;

import com.darkblue.DarkBlueHotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    //writing query to find room by available data and time

//selecting new room from our room table checking the checking dates and checking the rooms against the booking to make sure taht the check in check out passing to check
@Query("SELECT r FROM Room r WHERE r.roomType LIKE %:roomType AND r.id NOT IN (SELECT bk.room.id FROM Booking bk WHERE "+
        "(bk.checkInDate <= :checkOutDate) AND (bk.checkOutDate >= :checkInDate))")
    List<Room> findAvailableRoomsByDatesAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
//if one booked already it can't be booked for another



    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> getAllAvailableRooms();

}
