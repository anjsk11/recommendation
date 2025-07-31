package com.sensingbros.recommendation.rest;

import com.sensingbros.recommendation.model.UsersDTO;
import com.sensingbros.recommendation.model.ResponseDTO;
import com.sensingbros.recommendation.model.GpsDTO;
import com.sensingbros.recommendation.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    private JwtDecoder jwtDecoder;
    private final UsersService userService;

    // UserService 주입
    public UsersController(UsersService userService, JwtDecoder jwtDecoder) {
        this.userService = userService;
        this.jwtDecoder = jwtDecoder;
    }

    // POST 요청: UserDto 저장
    @PostMapping
    public ResponseEntity<ResponseDTO<String>> syncUser(@RequestBody UsersDTO usersDTO, @AuthenticationPrincipal Jwt jwt) {
        try {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
            // UsersDTO에서 idToken 꺼내기
            String idToken = usersDTO.getIdToken();
            // idToken을 JwtDecoder로 디코딩
            Jwt idTokenJwt = jwtDecoder.decode(idToken);
            // name 클레임 추출
            String name = idTokenJwt.getClaim("name");
            userService.syncUser(userId, name);
            return ResponseEntity.ok(new ResponseDTO<>(true));
        }catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO<>(false, e.getMessage()));
        }
    }

    // id로 users 정보 가져오기 (name, email)
    @GetMapping("/me")
    public ResponseEntity<ResponseDTO<?>> getUser(@PathVariable UUID id) {
        try {
            Optional<UsersDTO> usersDTOOptional = userService.getUserById(id);
            if (usersDTOOptional.isPresent()) {
                UsersDTO usersDTO = usersDTOOptional.get();
                return ResponseEntity.ok(new ResponseDTO<>(true, usersDTO));
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(false, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(false, e.getMessage()));
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<ResponseDTO<String>> deleteMyAccount(@AuthenticationPrincipal Jwt jwt) {
        try {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
            userService.deleteUserById(userId);
            return ResponseEntity.ok(new ResponseDTO<>(true));
        }catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO<>(false, e.getMessage()));
        }
    }

    @PostMapping("/me/heatmap")
    public ResponseEntity<ResponseDTO<String>> mapGps(@RequestBody GpsDTO gpsDto, @AuthenticationPrincipal Jwt jwt) {
        try {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
            double latitude = gpsDto.getLatitude();
            double longitude = gpsDto.getLongitude();
            userService.updateUserHeatmap(userId, latitude, longitude);
            return ResponseEntity.ok(new ResponseDTO<>(true));
        }catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO<>(false, e.getMessage()));
        }
    }
}
