package com.example.school.service.account;

import com.example.school.dto.UserDto;
import com.example.school.entity.Teacher;
import com.example.school.entity.User;
import com.example.school.form.user.CreatingUserForm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserTeacherService {

    public void save(CreatingUserForm creatingUserForm);


    public Teacher saveAccountTeacherId(CreatingUserForm creatingUserForm,int idTeacher);

    public void delete(int id);
    public User resetPassword(int id);

    public User blockUser(int id);
    public User enableUser(int id);
    public User finByIdUser(int id);

    public UserDto findByUsername(String username);

    public UserDto finById(int id);

    public UserDto getUserExist(String usernameNew, String usernameOrigin);

    public Page<UserDto> findPaginated(int pageNo, int pageSize,String keyword);
}
