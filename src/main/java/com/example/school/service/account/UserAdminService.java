package com.example.school.service.account;

import com.example.school.dto.UserDto;
import com.example.school.entity.User;
import com.example.school.form.user.CreatingUserForm;

import java.util.List;

public interface UserAdminService {

    List<User> getAllAccountAdmin();

    public void save(CreatingUserForm creatingUserForm);
    public void delete(int id);
    public User resetPassword(int id);

    public User finByIdUser(int id);

    public UserDto findByUsername(String username);

    public UserDto finById(int id);

    public UserDto getUserExist(String usernameNew, String usernameOrigin);
}
