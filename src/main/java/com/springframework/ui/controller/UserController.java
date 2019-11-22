package com.springframework.ui.controller;

import com.springframework.service.UserService;
import com.springframework.shared.dto.UserDto;
import com.springframework.ui.transfer.request.UserDetailsRequestModel;
import com.springframework.ui.transfer.response.OperationStatusModel;
import com.springframework.ui.transfer.response.RequestOperationName;
import com.springframework.ui.transfer.response.RequestOperationStatus;
import com.springframework.ui.transfer.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{userId}")
    public UserRest getUser(@PathVariable String userId) {

        UserDto userDto = userService.getUserByUserId(userId);

        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(userDto, userRest);

        return userRest;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
                 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto createdUser = userService.createUser(userDto);

        UserRest returnValue = new UserRest();
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping(path = "/{userId}",
                consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
                produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel, @PathVariable String userId) {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto updateUser = userService.updateUser(userId, userDto);

        UserRest returnValue = new UserRest();
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public OperationStatusModel deleteUser(@PathVariable String userId) {

        OperationStatusModel operationStatusModel = new OperationStatusModel();

        userService.deleteUser(userId);

        operationStatusModel.setOperationName(RequestOperationName.DELETE.name());
        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return operationStatusModel;
    }

}
