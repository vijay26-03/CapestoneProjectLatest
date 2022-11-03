package com.example.capestoneproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String firstname;
    private String lastname;
    @Id
    private String email;
    private String password;
    private String gender;
    private String dob;
    private String role ="role_user";
    private String verifyCode;
    private Boolean accessed = false;
}
