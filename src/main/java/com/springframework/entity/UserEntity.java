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
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;
    private String emailVerificationToken;

    // set default value to false
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean emailVerificationStatus;


}
