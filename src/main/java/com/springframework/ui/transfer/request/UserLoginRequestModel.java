package com.springframework.ui.transfer.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestModel {

    private String email;
    private String password;


}
