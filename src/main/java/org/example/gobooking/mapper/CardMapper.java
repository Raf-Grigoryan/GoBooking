package org.example.gobooking.mapper;

import org.example.gobooking.customException.UserNotFoundException;
import org.example.gobooking.dto.card.SaveCardRequest;
import org.example.gobooking.entity.user.Card;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CardMapper {

    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "user", expression = "java(findUserById(request.getUserId()))")
    public abstract Card toEntity(SaveCardRequest request);

    protected User findUserById(int userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found with " + userId +" id"));
    }
}