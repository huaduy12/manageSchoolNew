package com.example.school.service.account;

import com.example.school.dto.UserDto;
import com.example.school.entity.Role;
import com.example.school.entity.User;
import com.example.school.repository.UserRepository;
import com.example.school.security.EntityUserDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDto getUserByUsernameDto(String username) {
        User user = getUserByUsername(username);
        UserDto userDto = null;
        if(user != null){
            userDto = modelMapper.map(user,UserDto.class);
        }
        return userDto;
    }

    @Override
    public boolean changePassword(String passOld, String passNew, String passConfirm, String username) {
        User user =getUserByUsername(username);
        passNew =passNew.trim();
        if(!passNew.equals(passConfirm)) {
            return false;
        }else {
            if(passwordEncoder.matches(passOld,user.getPassword())){
                passNew = passwordEncoder.encode(passNew);
                user.setPassword(passNew);
                userRepository.save(user);
                return true;
            }else{
                return false;
            }
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // security: lấy username từ database xong tý nữa so sánh
        User user = getUserByUsername(username);
        if (user != null){
            // trả về entityUserDetail chứa user do mk tự đinh nghĩa
            return new EntityUserDetail(user);
        }else {
            throw  new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        Collection< ? extends GrantedAuthority> mapRoles =
                roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return mapRoles;
    }
}
