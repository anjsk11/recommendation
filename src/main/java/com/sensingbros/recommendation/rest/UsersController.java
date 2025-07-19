package com.sensingbros.recommendation.rest;

import com.sensingbros.recommendation.model.UsersDTO;
import com.sensingbros.recommendation.model.TFResponseDTO;
import com.sensingbros.recommendation.service.UsersService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TFResponseDTO> syncUser(@RequestBody UsersDTO userDto) {
        try {
            // UserDto를 저장하고 결과 UserDto 반환
            userService.syncUser(userDto);
            return ResponseEntity.ok(new TFResponseDTO(true));
        }catch (Exception e) {
            return ResponseEntity.ok(new TFResponseDTO(false));
        }
    }

    // id로 users 정보 가져오기 (name, email)
    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUser(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
