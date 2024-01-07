package com.example.school.repository;

import com.example.school.entity.Teacher_class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherClassRepository extends JpaRepository<Teacher_class,Integer> {

    Teacher_class findByRoleEqualsAndClassroom_Id(int role, int class_id);
    List<Teacher_class> findByClassroom_IdAndSubjectNotNull(int class_id);
    List<Teacher_class> findByTeacher_Id(int teacher_id);


    // dùng để kiểm tra phân công giảng daỵ cho trường hợp phân công bị trùng
    @Query("select t from Teacher_class t where t.school_year.id = ?1 and t.semester.id = ?2 and t.classroom.id = ?3 and t.subject.id = ?4")
    Teacher_class findByAssign(int schoolYear_id,int semester_id,int class_id, int subject_id);

    @Query("select t from Teacher_class t where t.subject is not null and t.teacher.id = ?1 and concat(t.subject.id,' ',t.subject.name,' ',t.classroom.name,' ',t.school_year.name,' ',t.semester.name) like %?2% ")
    Page<Teacher_class> findByTeacher_IdAndSubjectNotNull(int teacher_id,String keyword,Pageable pageable);
    Page<Teacher_class> findBySubject_Id(int subject_id, Pageable pageable);
    List<Teacher_class> findByRoleEqualsAndTeacher_Id(int role, int teacher_id);

    @Query("select t from Teacher_class t where t.role = ?1 and t.teacher.id = ?2 and concat(t.classroom.name,' ',t.school_year.name,' ') like %?3% ")
    Page<Teacher_class> findTeacherHomeroom(int role,int teacher_id, String keyword,Pageable pageable);
}
