package com.sensingbros.recommendation.service;

import com.sensingbros.recommendation.model.UsersDTO;
import com.sensingbros.recommendation.domain.Users;
import com.sensingbros.recommendation.mapper.UsersMapper;
import com.sensingbros.recommendation.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {

    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersMapper usersMapper, UsersRepository usersRepository) {
        this.usersMapper = usersMapper;
        this.usersRepository = usersRepository;
    }

//    // id로 User 조회 후 UserDto로 변환하여 반환
//    public UsersDTO getUser(Integer id) {
//        // 실제 DB 유저 엔티티 조회 (예시로 간단히 생성)
//        Users user = new Users();
//        user.setId(id);
//
//        // User 엔티티 → UserDto로 변환
//        return usersMapper.toDto(user);
//    }

//    public void syncUser(UUID id) {
//        if (!usersRepository.existsById(id)) {
//            Users user = new Users();
//            usersRepository.save(user);
//        }
//    }

    // UserDto를 User 엔티티로 변환하여 저장 (예시로 DB에 저장)
    public void syncUser(UsersDTO userDto) {
        // UserDto → User 엔티티로 변환
        Users user = usersMapper.toEntity(userDto);
        UUID id = userDto.getId(); // userDto 안의 id 가져오기

        if (!usersRepository.existsById(id)) { usersRepository.save(user); }  // DB에 저장
    }

    // DB에서 데이터를 받아와 UsersDTO로 변환
    public Optional<UsersDTO> getUserById(UUID id) {
        return usersRepository.findById(id)
                .map(usersMapper::toDto);
    }
}