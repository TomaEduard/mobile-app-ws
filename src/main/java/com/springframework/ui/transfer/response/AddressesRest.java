package com.springframework.ui.transfer.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

@Getter
@Setter
public class AddressesRest extends ResourceSupport {

    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
//    private String userDetails;

}
