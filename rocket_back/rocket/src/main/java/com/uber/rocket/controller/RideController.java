package com.uber.rocket.controller;

import com.uber.rocket.dto.DatePeriod;
import com.uber.rocket.dto.NewReviewDTO;
import com.uber.rocket.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ride")
public class RideController {
    private final RideService rideService;
    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRideDetails(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(rideService.getRideDetails(id));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
    @GetMapping("/favourite")
    public ResponseEntity<?> getFavouriteRoutesForUser(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(rideService.getFavouriteRoutesForUser(request));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/favourite/{rideId}")
    public ResponseEntity<?> addFavouriteRoute(HttpServletRequest request, @PathVariable Long rideId) {
        try {
            return ResponseEntity.ok(rideService.addFavouriteRoute(request, rideId));
        }
        catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/review")
    public ResponseEntity<?> addReview(HttpServletRequest request, @RequestBody NewReviewDTO dto) {
        try {
            return ResponseEntity.ok(rideService.addReview(request, dto));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("/favourite/{favouriteRouteId}")
    public ResponseEntity<?> deleteFavouriteRoute(@PathVariable Long favouriteRouteId) {
        try {
            rideService.deleteFavouriteRoute(favouriteRouteId);
            return ResponseEntity.ok("Delete successful.");
        }
        catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{size}/{page}")
    public ResponseEntity<?> getRideHistoryForUser(HttpServletRequest request, @PathVariable int page, @PathVariable int size) {
        try {
            return ResponseEntity.ok(rideService.getRideHistoryForUser(request, page, size));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/all/{size}/{page}")
    public ResponseEntity<?> getAllRideHistory(@PathVariable int page, @PathVariable int size) {
        try {
            return ResponseEntity.ok(rideService.getAllRideHistory(page, size));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/report/{type}")
    public ResponseEntity<?> getReportForUser(HttpServletRequest request, @PathVariable String type, @RequestBody DatePeriod datePeriod) {
        try {
            return ResponseEntity.ok(rideService.getReportForUser(request, type, datePeriod));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/report/all/{type}")
    public ResponseEntity<?> getReportForAll(@PathVariable String type, @RequestBody DatePeriod datePeriod) {
        try {
            return ResponseEntity.ok(rideService.getReportForAll(type, datePeriod));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
