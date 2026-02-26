package com.darkblue.DarkBlueHotel.service.interfac;

import com.darkblue.DarkBlueHotel.dto.Response;
import com.darkblue.DarkBlueHotel.entity.Booking;

public interface IBookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);

}
