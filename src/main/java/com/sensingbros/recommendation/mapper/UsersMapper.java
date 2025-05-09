package com.sensingbros.recommendation.mapper;
import com.sensingbros.recommendation.dto.UsersDto;
import com.sensingbros.recommendation.entity.Users;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    private final ModelMapper modelMapper;

    // ModelMapper를 주입
    public UsersMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // User 엔티티 → UserDto 매핑
    public UsersDto toDto(Users user) {
        return modelMapper.map(user, UsersDto.class);
    }

    // UserDto → User 엔티티 매핑
    public Users toEntity(UsersDto userDto) {
        return modelMapper.map(userDto, Users.class);
    }
}
