package com.darkblue.DarkBlueHotel.service.interfac;

import com.darkblue.DarkBlueHotel.dto.LoginRequest;
import com.darkblue.DarkBlueHotel.dto.Response;
import com.darkblue.DarkBlueHotel.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

}

