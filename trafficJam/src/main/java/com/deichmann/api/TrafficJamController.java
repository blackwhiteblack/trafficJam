package com.deichmann.api;

import com.deichmann.model.TrafficJam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@RestController
@RequestMapping("/traffic")
@RequiredArgsConstructor
@Slf4j
public class TrafficJamController {

    private final TrafficJam trafficJam;

    @GetMapping("/print/{code}")
    public ResponseEntity<String> print(@PathVariable final String code) {
        return processRequest(code, () -> {
            log.info("Printing traffic jam with code: {}", code);
            return trafficJam.print();
        });
    }

    @GetMapping("/turnToHead/{code}")
    public ResponseEntity<String> turnToHead(@PathVariable final String code) {
        return processRequest(code, () -> {
            trafficJam.turnToHead();
            log.info("Turning traffic jam to head with code: {}", code);
            return trafficJam.print();
        });
    }

    @GetMapping("/removeFirstVehicle/{code}")
    public ResponseEntity<String> removeFirstVehicle(@PathVariable final String code) {
        return processRequest(code, () -> {
            trafficJam.removeFirst();
            log.info("Removing first vehicle from traffic jam with code: {}", code);
            return trafficJam.print();
        });
    }

    @GetMapping("/removeLastVehicle/{code}")
    public ResponseEntity<String> removeLastVehicle(@PathVariable final String code) {
        return processRequest(code, () -> {
            trafficJam.removeLast();
            log.info("Removing last vehicle from traffic jam with code: {}", code);
            return trafficJam.print();
        });
    }

    @GetMapping("/fillTrucks/{code}")
    public ResponseEntity<String> fillTrucks(@PathVariable final String code) {
        return processRequest(code, () -> {
            trafficJam.print();
            trafficJam.fillTrucks();
            log.info("Filling trucks in traffic jam with code: {}", code);
            return trafficJam.print();
        });
    }

    private ResponseEntity<String> processRequest(final String code, final Supplier<String> action) {
        if (code == null || !isValidCodeFormat(code)) {
            log.error("Invalid code format: {}", code);
            return ResponseEntity.badRequest().body("Invalid code format");
        }
        try {
            trafficJam.setCode(code);
            return ResponseEntity.ok(action.get());
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    private boolean isValidCodeFormat(final String code) {
        return code.matches("[CBPT]+");
    }
}
