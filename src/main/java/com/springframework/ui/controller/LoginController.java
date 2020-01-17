//package com.springframework.ui.controller;
//
//import com.fasterxml.jackson.annotation.JsonView;
//import com.springframework.exceptions.error.ApiError;
//import com.springframework.io.entity.UserEntity;
//import com.springframework.ui.transfer.response.CurrentUser;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.nio.file.AccessDeniedException;
//
//@RestController
//public class LoginController {
//
//    @PostMapping("/users/login")
//    UserEntity handleLogin(@CurrentUser UserEntity loggedInUser) {
//        System.out.println("TEST - /users/login");
//        return loggedInUser;
//    }
//
//    @ExceptionHandler({AccessDeniedException.class})
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    ApiError handleAccessDeniedException(MethodArgumentNotValidException exception, HttpServletRequest request) {
//        return new ApiError(401, "Access error", "/users/login");
//    }
//}
