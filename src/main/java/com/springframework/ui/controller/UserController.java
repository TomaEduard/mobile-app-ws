package com.springframework.ui.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.springframework.dto.UserDto;
import com.springframework.service.UserService;
import com.springframework.ui.transfer.request.UserDetailsRequestModel;
import com.springframework.ui.transfer.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String getUser() {
        return "get user was callerd";
    }

    @PostMapping
    public UserRest createUset(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto createdUser = userService.createUser(userDto);

        UserRest returnValue = new UserRest();
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser() {

        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {

        return "delete user was called";
    }

}
