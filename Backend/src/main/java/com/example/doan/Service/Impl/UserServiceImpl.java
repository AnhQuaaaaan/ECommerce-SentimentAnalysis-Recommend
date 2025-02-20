package com.example.doan.Service.Impl;

import com.example.doan.Config.UserDetailsImpl;
import com.example.doan.Dto.UserDto;
import com.example.doan.Entity.User;
import com.example.doan.Entity.num.Role;
import com.example.doan.Repository.UserRepository;
import com.example.doan.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public boolean register(UserDto customerDto) {
        if (userRepository.findUserByUsernameAndPassword(customerDto.getUsername(), customerDto.getPassword())!=null){
            return false;
        }
        User user=convertToUser(customerDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDto findUserByUserName(String username) {
        User user=userRepository.findUserByUsername(username);
        return convertToUserDto(user);
    }

    @Override
    public UserDto getUserById(String id) {
        Optional<User> userOptional=userRepository.findById(id);
        User user=userOptional.get();
        return convertToUserDto(user);
    }

    public UserDto convertToUserDto(User user){
        UserDto userDto=new UserDto();
        userDto.setId(user.getId());
        userDto.setDob(user.getDob());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());
        userDto.setAddress(user.getAddress());
        userDto.setEmail(user.getEmail());
        userDto.setFullname(user.getFullname());
        userDto.setUsername(user.getUsername());
        return userDto;
    }
    public User convertToUser(UserDto userDto){
        User user=new User();
        user.setId(generateCustomerId());
        user.setDob(userDto.getDob());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setFullname(userDto.getFullname());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return user;
    }
    private String generateCustomerId() {
        StringBuilder builder = new StringBuilder("A");
        Random random = new Random();
        String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 1; i < 14; i++) {
            int index = random.nextInt(alphaNumeric.length());
            builder.append(alphaNumeric.charAt(index));
        }

        return builder.toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user);
    }
}
