package com.example.school.repository;

import com.example.school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    public List<Student> findAllByClassroom_Id(int classId);
    public Student findByUser_Id(int user_id);

    @Query("select s.fullName from Student as s where s.id = ?1 ")
    public String findFullnameById(int id);
}
