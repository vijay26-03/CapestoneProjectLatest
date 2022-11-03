package com.example.capestoneproject.service;

import com.example.capestoneproject.entity.User;



import java.util.List;

public interface UserService {
    User SaveUser(User user);
    User GetUser(String email);
    boolean Checkemail(String email);
    boolean Checkpassword(String password);
}
