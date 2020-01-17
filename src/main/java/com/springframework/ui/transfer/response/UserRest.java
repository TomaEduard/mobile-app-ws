package com.springframework.ui.transfer.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRest {

    private String userId;
    private String displayName;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private List<AddressesRest> addresses;

}
