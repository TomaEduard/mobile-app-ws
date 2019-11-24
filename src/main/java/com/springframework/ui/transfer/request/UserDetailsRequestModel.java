package com.springframework.ui.transfer.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class UserDetailsRequestModel {

    private String firstName;
    private String lastName;
    private String email;
    @ToString.Exclude
    private String password;

    private List<AddressRequestModel> addresses;
}
