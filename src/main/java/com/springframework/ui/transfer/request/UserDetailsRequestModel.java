package com.springframework.ui.transfer.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserDetailsRequestModel {

    @NotNull
    @Size(min = 4, max = 255)
    private String displayName;

    @NotNull
    @Size(min = 4, max = 255)
    private String firstName;

    @NotNull
    @Size(min = 4, max = 255)
    private String lastName;

    @NotNull
    @Size(min = 4, max = 255)
    private String email;

    @ToString.Exclude
    @NotNull
    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$")
    private String password;

    private String image;

    private List<AddressRequestModel> addresses;
}
