package com.prem.userservice.mappings;

import com.prem.userservice.dto.UserDto;
import com.prem.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "name", target = "name")
    UserDto userToUserDto(User user);
}
