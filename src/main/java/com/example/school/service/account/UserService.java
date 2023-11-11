package com.example.school.service.account;

import com.example.school.dto.UserDto;
import com.example.school.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService{

    public User getUserByUsername(String username);
    public UserDto getUserByUsernameDto(String username);

    public boolean changePassword(String passOld,String passNew,String passConfirm, String username);

}
