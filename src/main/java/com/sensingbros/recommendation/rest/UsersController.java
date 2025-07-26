package com.sensingbros.recommendation.rest;

import com.sensingbros.recommendation.model.UsersDTO;
import com.sensingbros.recommendation.model.ResponseDTO;
import com.sensingbros.recommendation.model.GpsDTO;
import com.sensingbros.recommendation.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService userService;

    // UserService 주입
    public UsersController(UsersService userService) {
        this.userService = userService;
    }

    // POST 요청: UserDto 저장
    @PostMapping
    public ResponseEntity<ResponseDTO> syncUser(@AuthenticationPrincipal Jwt jwt) {
        try {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
            userService.syncUser(userId);
            return ResponseEntity.ok(new ResponseDTO(true));
        }catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false));
        }
    }

    // id로 users 정보 가져오기 (name, email)
    @GetMapping("/me")
    public ResponseEntity<UsersDTO> getUser(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/me")
    public ResponseEntity<ResponseDTO> deleteMyAccount(@AuthenticationPrincipal Jwt jwt) {
//        try {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
            userService.deleteUserById(userId);
            return ResponseEntity.ok(new ResponseDTO(true));
//        }catch (Exception e) {
//            return ResponseEntity.ok(new TFResponseDTO(false));
//        }
    }

    @PostMapping("/me/heatmap")
    public ResponseEntity<ResponseDTO> mapGps(@RequestBody GpsDTO gpsDto, @AuthenticationPrincipal Jwt jwt) {
        try {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
            double latitude = gpsDto.getLatitude();
            double longitude = gpsDto.getLongitude();
            userService.updateUserHeatmap(userId, latitude, longitude);
            return ResponseEntity.ok(new ResponseDTO(true));
        }catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false));
        }
    }

}
