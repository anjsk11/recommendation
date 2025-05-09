package com.sensingbros.recommendation.service;

import com.sensingbros.recommendation.dto.UsersDto;
import com.sensingbros.recommendation.entity.Users;
import com.sensingbros.recommendation.mapper.UsersMapper;
import com.sensingbros.recommendation.repository.UsersRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersMapper usersMapper, UsersRepository usersRepository) {
        this.usersMapper = usersMapper;
        this.usersRepository = usersRepository;
    }

    // id로 User 조회 후 UserDto로 변환하여 반환
    public UsersDto getUser(Integer id) {
        // 실제 DB에서 유저 엔티티 조회 (예시로 간단히 생성)
        Users user = new Users();
        user.setId(id);

        // User 엔티티 → UserDto로 변환
        return usersMapper.toDto(user);
    }

    // UserDto를 User 엔티티로 변환하여 저장 (예시로 DB에 저장)
    public UsersDto saveUser(UsersDto userDto) {
        // UserDto → User 엔티티로 변환
        Users user = usersMapper.toEntity(userDto);

        Users savedUser = usersRepository.save(user);  // DB에 저장

        // 저장된 엔티티를 DTO로 변환하여 반환
        return usersMapper.toDto(savedUser);  // 엔티티를 DTO로 변환하여 반환
    }
}

//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class UsersService {
//
//    @Autowired
//    private UsersRepository usersRepository;
//
//    public void saveUser(UsersDto dto) {
//        Users entity = dtoToEntity(dto);         // DTO → Entity
//        usersRepository.save(entity);
//    }
//
//    public List<UsersDto> getAllUsers() {
//        List<Users> users = usersRepository.findAll();
//        return users.stream()
//                .map(this::entityToDto)          // Entity → DTO
//                .collect(Collectors.toList());
//    }
//
//    private Users dtoToEntity(UsersDto dto) {
//        Users user = new Users();
//        user.setId(dto.getId());
//        return user;
//    }
//
//    private UsersDto entityToDto(Users entity) {
//        UsersDto dto = new UsersDto();
//        dto.setId(entity.getId());
//        return dto;
//    }
//}
