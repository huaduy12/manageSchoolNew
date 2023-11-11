package com.example.school.repository;

import com.example.school.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Integer> {

    List<Score> findAllByStudent_Id(int student_id);

    @Query("select s from Score s where s.school_year.id = ?1 and s.semester.id = ?2 and s.student.id = ?3 and s.subject.id = ?4 ")
    Score findByStudentIdAndSubjectId(int schoolYear_Id, int semester_Id,int studentId, int subjectId);
}
