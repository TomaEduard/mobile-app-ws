package com.springframework.service.impl;

import com.springframework.UserRepository;
import com.springframework.dto.UserDto;
import com.springframework.entity.UserEntity;
import com.springframework.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

        UserEntity storeUserDetails = userRepository.findByEmail(userDto.getEmail());

        if(userRepository.findByEmail(userDto.getEmail()) != null) throw new RuntimeException("Record is already exists!");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        userEntity.setEncryptedPassword("test");
        userEntity.setUserId("testUserId");

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }
}