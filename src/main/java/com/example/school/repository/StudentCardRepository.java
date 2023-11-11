package com.example.school.repository;

import com.example.school.entity.Student;
import com.example.school.entity.StudentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCardRepository extends JpaRepository<StudentCard,Integer> {

    public StudentCard findByStudent(Student student);
}
