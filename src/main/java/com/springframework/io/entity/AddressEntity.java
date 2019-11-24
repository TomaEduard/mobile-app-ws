package com.springframework.io.entity;

import com.springframework.shared.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.io.Serializable;

@Setter
@Getter
@ToString
@Entity
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = -307024434219926395L;

    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    private UserDto userDetails;

}
