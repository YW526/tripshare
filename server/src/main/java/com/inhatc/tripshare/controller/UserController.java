package com.inhatc.tripshare.controller;

import com.inhatc.tripshare.dto.TripResponse;
import com.inhatc.tripshare.dto.UserResponse;
import com.inhatc.tripshare.service.TripShareService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final TripShareService service;

    public UserController(TripShareService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public UserResponse profile(@PathVariable Long userId) {
        return service.getUser(userId);
    }

    @GetMapping("/{userId}/trips/created")
    public List<TripResponse> createdTrips(@PathVariable Long userId) {
        return service.getMyTrips(userId);
    }

    @GetMapping("/{userId}/trips/saved")
    public List<TripResponse> savedTrips(@PathVariable Long userId) {
        return service.getSavedTrips(userId);
    }

    @PostMapping("/{userId}/follow/{targetId}")
    public Map<String, String> follow(@PathVariable Long userId, @PathVariable Long targetId) {
        service.follow(userId, targetId);
        return Map.of("message", "followed");
    }
}
