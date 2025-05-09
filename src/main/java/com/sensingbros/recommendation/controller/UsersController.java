package com.sensingbros.recommendation.controller;

import com.sensingbros.recommendation.dto.UsersDto;
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

    // GET 요청: id로 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<UsersDto> getUser(@PathVariable Integer id) {
        // id로 유저 정보 조회 후 UserDto 반환
        UsersDto userDto = userService.getUser(id);
        return ResponseEntity.ok(userDto);
    }

    // POST 요청: UserDto 저장
    @PostMapping
    public ResponseEntity<UsersDto> saveUser(@RequestBody UsersDto userDto) {
        // UserDto를 저장하고 결과 UserDto 반환
        UsersDto savedUser = userService.saveUser(userDto);
        return ResponseEntity.ok(savedUser);
    }
    //test
}

