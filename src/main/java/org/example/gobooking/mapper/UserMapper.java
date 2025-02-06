package org.example.gobooking.mapper;

import org.example.gobooking.dto.user.UserDto;
import org.example.gobooking.entity.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
}
