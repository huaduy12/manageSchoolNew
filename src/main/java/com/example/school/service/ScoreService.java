package com.example.school.service;

import com.example.school.dto.ScoreDto;
import com.example.school.entity.Score;

import java.util.List;

public interface ScoreService {

    public List<ScoreDto> getAllScore();
    public List<ScoreDto> getAllStudentId(int id);
    public ScoreDto findByIdDto(int id);
    public Score findById(int id);
    public Score findByStudentIdAndSubjectId(int schoolId,int semesterId, int studentId, int subjectId);

    public List<ScoreDto> findScoreByClassroomAndSubject(int idTeacher_class);

    // lưu điểm môn học của học sinh khi vào xem bảng điểm học sinh đó
    public Boolean saveScoreSubject(ScoreDto scoreDto);

    public Boolean save(Score score);
    public void delete(Score score);
}
