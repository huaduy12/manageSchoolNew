package com.example.school.repository;

import com.example.school.entity.RoleTeacher;
import com.example.school.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Integer> {
    Teacher findByUser_Id(int userId);

    Page<Teacher> findAllByRoleNot(RoleTeacher roleTeacher, Pageable pageable);


}
