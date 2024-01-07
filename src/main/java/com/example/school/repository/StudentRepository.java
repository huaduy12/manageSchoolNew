package com.example.school.repository;

import com.example.school.entity.RoleTeacher;
import com.example.school.entity.Student;
import com.example.school.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    public List<Student> findAllByClassroom_Id(int classId);
    public Student findByUser_Id(int user_id);
    public int countAllByClassroom_Id(int class_id);
    @Query("select s.fullName from Student as s where s.id = ?1 ")
    public String findFullnameById(int id);

    @Query("select t from Student t where (concat(t.fullName,' ',t.birthday,' ',t.academy_year,' ',t.classroom.name) like %?1%)")
    Page<Student> findAll(String keyword,Pageable pageable);
}
