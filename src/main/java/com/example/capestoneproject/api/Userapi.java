package com.example.capestoneproject.api;

import com.example.capestoneproject.entity.User;
import com.example.capestoneproject.jwt.JwtManager;
import com.example.capestoneproject.jwt.JwtRequestModel;
import com.example.capestoneproject.jwt.JwtResponseModel;
import com.example.capestoneproject.jwt.JwtUserDetailsService;
import com.example.capestoneproject.service.EmailverificationServices;
import com.example.capestoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.URI;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Userapi {
    private final UserService userService;
    private final EmailverificationServices emailVerify;

    private final JwtUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtManager jwtManager;


    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody JwtRequestModel request) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwtToken = jwtManager.generateJwtToken(userDetails);
        return ResponseEntity.ok(new JwtResponseModel(jwtToken));
    }


    @PostMapping("/user/save")
    public ResponseEntity<User>saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.SaveUser(user));
    }


    @PostMapping("/user/signup")
    public ResponseEntity<?>signup(@RequestBody User user) throws MessagingException, UnsupportedEncodingException {
        if(userService.GetUser(user.getEmail())==null){
            if((user.getFirstname() != null && !user.getFirstname().equals("")) && (user.getLastname() != null && !user.getLastname().equals("")) && (user.getPassword() != null && !user.getPassword().equals("")) && (user.getEmail() != null && !user.getEmail().equals("")) && (user.getGender() != null && !user.getGender().equals("")) && (user.getDob() != null)){
                if(userService.Checkemail(user.getEmail())){
                    if(userService.Checkpassword(user.getPassword())){
                        emailVerify.register(user);
                        userService.SaveUser(user);
                        return ResponseEntity.ok().body("verification link has been sent your mail");
                    }
                    return ResponseEntity.badRequest().body("your password is weak");
                }
                return ResponseEntity.badRequest().body("email is not valid");
            }
            return ResponseEntity.badRequest().body("fill all the  fields");
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("email already existed");
    }

    @GetMapping ("/verify/{email}")
    public ResponseEntity<?> userVerify(@PathVariable String email,@RequestParam String code){
        if(userService.GetUser(email).getVerifyCode().equals(code)) {
            User user = userService.GetUser(email);
            user.setAccessed(true);
            System.out.println(user);
            userService.SaveUser(user);
            System.out.println(userService.GetUser(email));
            return ResponseEntity.status(HttpStatus.OK).body("<h1>Account verified</h1>");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<h1>Verification code is invalid</h1>");
    }

}

