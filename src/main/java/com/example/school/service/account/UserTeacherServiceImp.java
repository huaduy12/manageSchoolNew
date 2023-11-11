package com.example.school.service.account;

import com.example.school.dto.UserDto;
import com.example.school.entity.Role;
import com.example.school.entity.Teacher;
import com.example.school.entity.User;
import com.example.school.form.user.CreatingUserForm;
import com.example.school.repository.RoleRepository;
import com.example.school.repository.UserRepository;
import com.example.school.service.TeacherServiceImp;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserTeacherServiceImp implements UserTeacherService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherServiceImp teacherServiceImp;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllAccountTeacher() {

        List<User> getAllUser = userRepository.findAll();
        List<User> users= new ArrayList<>();
        for (User u:getAllUser) {
            for (Role role : u.getRoles()) {
                if (role.getId() != 1 && role.getId() == 2){
                    users.add(u);
                }
            }
        }

        return users;
    }


    @Override
    @Transactional
    public void save(CreatingUserForm creatingUserForm) {
        Role  roleManager = roleRepository.findById(2);
        Role  roleUser = roleRepository.findById(3);
        creatingUserForm.setRoles(Arrays.asList(roleManager,roleUser));
        User user = modelMapper.map(creatingUserForm, User.class);
        // lấy từ form về trong trường hợp edit ko có pass, chi có id nên theo id tìm kiêm xong đặt pass
        if(creatingUserForm.getId() != 0){
            User userPass = finByIdUser(creatingUserForm.getId());
            user.setPassword(userPass.getPassword());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public Teacher saveAccountTeacherId(CreatingUserForm creatingUserForm, int idTeacher) {

        Teacher teacher = teacherServiceImp.findById(idTeacher);
        if(teacher != null){
            save(creatingUserForm);
            User userLast = userRepository.getLastUserOrOrderById();
            teacher.setUser(userLast);
            teacherServiceImp.save(teacher);
            return teacher;
        }
        return teacher;
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User resetPassword(int id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;
        if(result.isPresent()){
            user = result.get();
        }
        if (user != null){
            user.setPassword(passwordEncoder.encode(user.getUsername()+"123"));
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public User blockUser(int id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;
        if(result.isPresent()){
            user = result.get();
        }
        if (user != null){
            user.setActive(false);
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public User enableUser(int id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;
        if(result.isPresent()){
            user = result.get();
        }
        if (user != null){
            user.setActive(true);
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public User finByIdUser(int id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;
        if(result.isPresent()){
            user = result.get();
        }
        return user;
    }

    @Override
    public UserDto findByUsername(String username) {

        User user = userRepository.findByUsername(username.trim());
        UserDto userDto = null;
        if(user != null){
            userDto = modelMapper.map(user,UserDto.class);
        }
        return userDto;
    }

    @Override
    public UserDto finById(int id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;
        UserDto userDto = null;
        if(result.isPresent()){
            user = result.get();
        }
        if (user != null){
            userDto = modelMapper.map(user,UserDto.class);
        }
        return userDto;
    }

    @Override
    public UserDto getUserExist(String usernameNew, String usernameOrigin) {
        User user = userRepository.getUserExist(usernameNew.trim(),usernameOrigin.trim());
        UserDto userDto = null;
        if (user != null){
            userDto = modelMapper.map(user,UserDto.class);
        }
        return userDto ;
    }
}
