package com.sensingbros.recommendation.rest;

import com.sensingbros.recommendation.model.UsersDTO;
import com.sensingbros.recommendation.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UsersDTO> saveUser(@RequestBody UsersDTO userDto) {
        // UserDto를 저장하고 결과 UserDto 반환
        UsersDTO savedUser = userService.saveUser(userDto);
        return ResponseEntity.ok(savedUser);
    }
}
