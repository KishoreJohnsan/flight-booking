package com.flightapp.authservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class UserDet {

    @Id
    private String username;
    private String password;
    private String role;

}
