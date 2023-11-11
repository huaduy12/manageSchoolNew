package com.example.school.repository;

import com.example.school.entity.Role;
import com.example.school.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    public List<User> findByRolesEquals(Role role);

    @Query("select u from User u where u.username like ?1 and u.username not like ?2 ")
    public User getUserExist(String usernameNew, String usernameOrigin);

    @Query("select u from User u  where u.id = (select max(id) from User )")
    public User getLastUserOrOrderById();
    public User findByUsername(String username);
}
