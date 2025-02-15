package org.example.gobooking.mapper;

import org.example.gobooking.dto.user.UserDto;
import org.example.gobooking.dto.user.WorkerResponse;
import org.example.gobooking.entity.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    List<WorkerResponse> toDto(List<User> users);

    WorkerResponse toWorker(User user);
}
