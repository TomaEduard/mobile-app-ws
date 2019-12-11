package com.springframework.io.entity;

import com.springframework.shared.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString
@Entity(name = "addresses")
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = -307024434219926395L;

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 30, nullable = false)
    private String addressId;

    @Column(length = 15, nullable = false)
    private String city;

    @Column(length = 15, nullable = false)
    private String country;

    @Column(length = 100, nullable = false)
    private String streetName;

    @Column(length = 7, nullable = false)
    private String postalCode;

    @Column(length = 15, nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id") // userId
    private UserEntity userEntity;

}
