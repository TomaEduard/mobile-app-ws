package com.springframework.ui.controller;

import com.springframework.io.repository.UserRepository;
import com.springframework.service.AddressService;
import com.springframework.service.UserService;
import com.springframework.shared.dto.AddressDTO;
import com.springframework.shared.dto.UserDto;
import com.springframework.ui.transfer.request.UserDetailsRequestModel;
import com.springframework.ui.transfer.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users") // http://localhost:8081/users
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
                 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {

//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetailsRequestModel, userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetailsRequestModel, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

//        BeanUtils.copyProperties(createdUser, returnValue);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @GetMapping(path = "/{userId}")
    public UserRest getUser(@PathVariable String userId) {

        UserDto userDto = userService.getUserByUserId(userId);

        UserRest returnValue = new ModelMapper().map(userDto, (Type) UserRest.class);

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

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "20") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        // for every users retrieve create, copy properties to a userRest and add this to an ArrayList
        for (UserDto userDto : users) {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDto, userRest);
            returnValue.add(userRest);
        }

        return returnValue;
    }

    // http://localholst:8081/mobile-app-ws/users/{userId}/addresses
    @GetMapping(path = "/{userId}/addresses",
                produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public List<AddressesRest> getUserAddresses(@PathVariable String userId) {

        List<AddressDTO> addressDTO = addressService.getAddresses(userId);

        List<AddressesRest> addressesRestList = new ArrayList<>();

        if (addressDTO != null && !addressDTO.isEmpty()) {
            java.lang.reflect.Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
            addressesRestList = new ModelMapper().map(addressDTO, listType);
        }

        return addressesRestList;
    }

    // http://localholst:8081/mobile-app-ws/users/{userId}/addresses/{addressId}
    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public AddressesRest getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

        AddressDTO addressDTO = addressService.getAddress(addressId);

        return  new ModelMapper().map(addressDTO, AddressesRest.class);

    }
}
