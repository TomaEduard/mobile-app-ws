package com.springframework.io.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "users")
public class UserEntity implements Serializable {

//    @ToString.Exclude
    private static final long serialVersionUID = 6835192601898364280L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // require field, don't save in db if doesn't have userId
    // alphanumeric - sending back to movie application with response.verify
    @Column(nullable = false)                   // cannot be null
    private String  userId;

    @Column(nullable = false)                   // cannot be null
    private String displayName;

    @Column(nullable = false, length = 50)      // cannot be null & limit length
    private String firstName;

    @Column(nullable = false, length = 50)      // cannot be null & limit length
    private String lastName;

    @Column(nullable = false, length = 120)     // cannot be null & limit length & unique
    private String email;

    @Column(nullable = false)                   // cannot be null & limit length
    private String encryptedPassword;

    private String emailVerificationToken;

    // set default value to false
    @Column(nullable = false)                   // cannot be null & limit length
    private Boolean emailVerificationStatus =  false;

    private String image;

    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private List<AddressEntity> addresses;

}









