package com.example.doan.Controller;

import com.example.doan.Config.Jwt.JwtUtils;
import com.example.doan.Dto.JwtRequest;
import com.example.doan.Dto.UserDto;
import com.example.doan.Service.Impl.UserServiceImpl;
import com.example.doan.Service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/info/{id}")
    public ResponseEntity<?> Register(@PathVariable String id){
        UserDto userDto=userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }
}
