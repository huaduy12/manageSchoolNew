package com.example.school.repository;

import com.example.school.entity.Teacher_class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherClassRepository extends JpaRepository<Teacher_class,Integer> {

    public Teacher_class findByRoleEqualsAndClassroom_Id(int role, int class_id);
    public List<Teacher_class> findByClassroom_IdAndSubjectNotNull(int class_id);
    public List<Teacher_class> findByTeacher_Id(int teacher_id);
    public List<Teacher_class> findByTeacher_IdAndSubjectNotNull(int teacher_id);
    public Page<Teacher_class> findBySubject_Id(int subject_id, Pageable pageable);
    public List<Teacher_class> findByRoleEqualsAndTeacher_Id(int role, int teacher_id);

}
