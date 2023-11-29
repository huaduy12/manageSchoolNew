package com.example.school.repository;

import com.example.school.entity.Role;
import com.example.school.entity.Subject;
import com.example.school.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    public Page<User> findByRolesEquals(Role role, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles ur WHERE (concat(u.username,' ') like %?1%) and (ur.id = 2 AND NOT EXISTS " +
            "(SELECT 1 FROM User u2 JOIN u2.roles ur2 WHERE u2 = u AND ur2.id =1))")
    Page<User> findManagersWithoutAdmin(String keyword,Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles ur WHERE (concat(u.username,' ') like %?1%) and (ur.id = 3 AND NOT EXISTS " +
            "(SELECT 1 FROM User u2 JOIN u2.roles ur2 WHERE u2 = u AND ur2.id =2))")
    Page<User> findStudentsWithoutManager(String keyword,Pageable pageable);
    // 1: Admin, 2: Manager, 3: Student

    @Query("select t from User t where (concat(t.username,' ') like %?1%)")
    Page<User> findAll(String keyword, Pageable pageable);

    @Query("select u from User u where u.username like ?1 and u.username not like ?2 ")
    public User getUserExist(String usernameNew, String usernameOrigin);


    @Query("select u from User u  where u.id = (select max(id) from User )")
    public User getLastUserOrOrderById();
    public User findByUsername(String username);
}
