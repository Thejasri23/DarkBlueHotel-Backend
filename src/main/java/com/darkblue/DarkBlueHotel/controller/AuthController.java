package com.darkblue.DarkBlueHotel.controller;


import com.darkblue.DarkBlueHotel.dto.LoginRequest;
import com.darkblue.DarkBlueHotel.dto.Response;
import com.darkblue.DarkBlueHotel.dto.UserDTO;
import com.darkblue.DarkBlueHotel.entity.User;
import com.darkblue.DarkBlueHotel.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
