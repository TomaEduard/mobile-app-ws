package com.springframework.ui.controller;

import com.springframework.service.AddressService;
import com.springframework.service.UserService;
import com.springframework.shared.dto.AddressDto;
import com.springframework.shared.dto.UserDto;
import com.springframework.ui.transfer.request.PasswordResetModel;
import com.springframework.ui.transfer.request.PasswordResetRequestModel;
import com.springframework.ui.transfer.request.UserDetailsRequestModel;
import com.springframework.ui.transfer.response.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
//@CrossOrigin(origins = {"http://localhost:8088"})
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetailsRequestModel, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "20") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        Type listType = new TypeToken<List<UserRest>>(){}.getType();

        returnValue = new ModelMapper().map(users, listType);

        return returnValue;
    }

    //    http://localhost:8080/movie-app-ws/users/{userId}
    @GetMapping(path = "/{userId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    public UserRest getUser(@PathVariable String userId) {

        UserDto userDto = userService.getUserByUserId(userId);

        UserRest returnValue = new ModelMapper().map(userDto, (Type) UserRest.class);

        return returnValue;
    }

    //    http://localhost:8080/movie-app-ws/users/{userId}
    @PutMapping(path = "/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel, @PathVariable String userId) {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto updateUser = userService.updateUser(userId, userDto);

        UserRest returnValue = new ModelMapper().map(updateUser, UserRest.class);

        return returnValue;

    }

    //    http://localhost:8080/movie-app-ws/users/{userId}
    @DeleteMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    public OperationStatusModel deleteUser(@PathVariable String userId) {

        OperationStatusModel operationStatusModel = new OperationStatusModel();

        userService.deleteUser(userId);

        operationStatusModel.setOperationName(RequestOperationName.DELETE.name());
        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return operationStatusModel;
    }

    // http://localhost:8080/movie-app-ws/users/{userId}/addresses
    @GetMapping(path = "/{userId}/addresses",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    public Resources<Object> getUserAddresses(@PathVariable String userId) {

        List<AddressesRest> addressesListRestModel = new ArrayList<>();

        List<AddressDto> addressDTO = addressService.getAddresses(userId);

        if (addressDTO != null && !addressDTO.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {
            }.getType();
            addressesListRestModel = new ModelMapper().map(addressDTO, listType);

            // added 2 links for every address 
            for (AddressesRest addressRest : addressesListRestModel) {
                Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressRest.getAddressId())).withSelfRel();
                addressRest.add(addressLink);

                Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
                addressRest.add(userLink);
            }

        }

        return new Resources<>(Collections.singleton(addressesListRestModel));
    }

    // http://localhost:8080/movie-app-ws/users/{userId}/addresses/{addressId}
    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    public Resource<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

        AddressDto addressDTO = addressService.getAddress(addressId);

//        Link addressLink = linkTo(UserController.class).slash(userId).slash("addresses").slash(addressId).withSelfRel();
        Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();

        Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
//        Link addressesLink = linkTo(UserController.class).slash(userId).slash("addresses").withRel("addresses");

        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

        AddressesRest returnValue = new ModelMapper().map(addressDTO, AddressesRest.class);

        returnValue.add(addressLink);
        returnValue.add(userLink);
        returnValue.add(addressesLink);

        return new Resource<>(returnValue);
    }

    // http://localhost:8080/movie-app-ws/users/email-verification?token=sdfsdf
    @GetMapping(path = "/email-verification",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        try {
            boolean isVerified = userService.verifyEmailToken(token);

            if (isVerified) {
                System.out.println("SUCCESS");
                returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
            }

        } catch (Exception e) {
            System.out.println("FAILING");
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

    // http://localhost:8080/movie-app-ws/users/password-reset-request
    @PostMapping(path = "/password-reset-request",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        if (operationResult) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }

    // http://localhost:8080/movie-app-ws/users/password-reset
    @PostMapping(path = "/password-reset",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<OperationStatusModel> resetPassword(@RequestBody PasswordResetModel passwordRequestModel) {
        System.out.println("1asd");
        boolean operationResult = userService.resetPassword(passwordRequestModel.getToken(), passwordRequestModel.getPassword());

        OperationStatusModel returnValue = new OperationStatusModel();

        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if (operationResult) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

}









