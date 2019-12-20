package com.springframework.ui.transfer.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetModel {

    private String token;

    private String password;

}
