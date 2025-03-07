package org.example.gobookingcommon.mapper;


import org.example.gobookingcommon.dto.auth.UserAuthResponse;
import org.example.gobookingcommon.dto.user.UserDto;
import org.example.gobookingcommon.dto.user.WorkerResponse;
import org.example.gobookingcommon.entity.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    List<WorkerResponse> toDto(List<User> users);

    WorkerResponse toWorker(User user);

    UserAuthResponse loginResponse(User user);
}
