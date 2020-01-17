//package com.springframework.ui.controller;
//
//import com.springframework.io.entity.UserEntity;
//import com.springframework.ui.transfer.request.LoginRequestModel;
//import com.springframework.ui.transfer.response.CurrentUser;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import io.swagger.annotations.ResponseHeader;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Collections;
//import java.util.Map;
//
//@ApiOperation("User login for Swagger")
//@ApiResponses(value = {
//        @ApiResponse(code = 200,
//                message = "Response Headers",
//                responseHeaders = {
//                        @ResponseHeader(name = "authorization", description = "Bearer <JWT value here>", response = String.class),
//                        @ResponseHeader(name = "userId", description = "<Public User Id value here>", response = String.class)
//                }
//        )
//})
//@RestController
//public class AuthenticationController {
//
//    @PostMapping("/users/login")
//    public void theFakeLogin(@RequestBody LoginRequestModel loginRequestModel) {
//
//        throw new IllegalStateException("This method should not be called. This method is implemented by Spring Security");
//    }
//
////    TODO add body for /users/login endpoint
////    @PostMapping("/users/login")
////    Map<String, Object> handleLogin(@CurrentUser UserEntity loggedInUser) {
////        System.out.println("TEST");
////        return Collections.singletonMap("id", loggedInUser.getId());
////    }
//}
