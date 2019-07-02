package com.springframework.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Setter
@Getter
@Entity(name = "users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 6835192601898364280L;

    @Id
    @GeneratedValue
    private long id;

    // require field, don't save in db if doesn't have userId
    // alphanumeric - sending back to mobile application with response.verify
    @Column(nullable = false)                   // cannot be null
    private String  userId;

    @Column(nullable = false, length = 50)      // cannot be null & limit length
    private String firstName;

    @Column(nullable = false, length = 50)      // cannot be null & limit length
    private String lastName;

    @Column(nullable = false, length = 100)     // cannot be null & limit length & unique
    private String email;

    @Column(nullable = false)                   // cannot be null & limit length
    private String encryptedPassword;
    private String emailVerificationToken;

    // set default value to false
    @Column(nullable = false)                   // cannot be null & limit length
    private Boolean emailVerificationStatus =  false;


}
