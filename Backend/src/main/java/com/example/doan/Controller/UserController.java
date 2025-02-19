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
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
        try{
            Authentication authentication= authenticate(jwtRequest.getUsername(),jwtRequest.getPassword());
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                UserDto userDto=userService.findUserByUserName(userDetails.getUsername());
                String token = jwtUtils.generateToken(userDetails);
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("accessToken", token);
                responseBody.put("user", userDto);
                HttpHeaders headers = new HttpHeaders();
                return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        } catch (Exception e) {
            throw new Exception("Authentication failed", e);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody UserDto userDto){
        if (userService.register(userDto)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản đã tồn tại");
    }
}
