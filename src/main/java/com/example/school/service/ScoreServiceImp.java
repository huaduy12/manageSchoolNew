package com.example.school.service;

import com.example.school.dto.ScoreDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.Teacher_classDto;
import com.example.school.entity.Score;
import com.example.school.entity.Student;
import com.example.school.entity.Teacher_class;
import com.example.school.repository.ScoreRepository;
import com.example.school.repository.TeacherClassRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScoreServiceImp implements ScoreService{

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private TeacherClassRepository teacherClassRepository;
    @Autowired
    private StudentService studentService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ScoreDto> getAllScore() {
        List<Score> results = scoreRepository.findAll();
        List<ScoreDto> resultDtos = null;
        if(results != null){
            Type type = new TypeToken<List<ScoreDto>>(){}.getType();
            resultDtos = modelMapper.map(results,type);
        }
        return resultDtos;
    }

    @Override
    public List<ScoreDto> getAllStudentId(int id) {
        List<Score> scores = scoreRepository.findAllByStudent_Id(id);
        List<ScoreDto> scoreDtos = null;
        if(scores != null){
            Type type = new TypeToken<List<ScoreDto>>(){}.getType();
            scoreDtos = modelMapper.map(scores,type);
        }
        return scoreDtos;
    }

    @Override
    public ScoreDto findByIdDto(int id) {
        Optional<Score> result = scoreRepository.findById(id);
        Score score = null;
        ScoreDto scoreDto = null;
        if(result.isPresent()){
            score = result.get();
            scoreDto = modelMapper.map(score, ScoreDto.class);
        }
        return scoreDto;
    }

    @Override
    public Score findById(int id) {
        Optional<Score> result = scoreRepository.findById(id);
        Score score = null;
        if(result.isPresent()){
            score = result.get();
        }
        return score;
    }

    @Override
    public Score findByStudentIdAndSubjectId(int schoolId,int semesterId, int studentId, int subjectId) {
        return scoreRepository.findByStudentIdAndSubjectId(schoolId,semesterId,studentId,subjectId);
    }

    @Override
    public List<ScoreDto> findScoreByClassroomAndSubject(int idTeacher_class) {
        Teacher_classDto teacher_class = findByIdDtoTeacher_class(idTeacher_class);
        if(teacher_class != null){
            List<StudentDto> studentDtos = studentService.getStudentByClassIdDto(teacher_class.getClassroom().getId());
            List<Score> scores = new ArrayList<>();
            for (StudentDto s : studentDtos) {
                System.out.println(teacher_class.getSchool_year().getId() + " " +
                        teacher_class.getSemester().getId() + " " + s.getId() + " " + teacher_class.getSubject().getId());
                Score score = findByStudentIdAndSubjectId(teacher_class.getSchool_year().getId(),
                        teacher_class.getSemester().getId(),s.getId(),teacher_class.getSubject().getId());

                scores.add(score);
            }
            List<ScoreDto> scoreDtos = null;
            if(scores != null){
                Type type = new TypeToken<List<ScoreDto>>(){}.getType();
                scoreDtos = modelMapper.map(scores,type);
            }
            return scoreDtos;
        }
        return null;
    }

    @Override
    public Boolean saveScoreSubject(ScoreDto scoreDto) {
        if(scoreDto == null){
            return false;
        }
        Score saveScore = findById(scoreDto.getId());

        saveScore.setScore_15(scoreDto.getScore_15());
        saveScore.setOval_score(scoreDto.getOval_score());
        saveScore.setScore_1_period(scoreDto.getScore_1_period());
        saveScore.setTest_score(scoreDto.getTest_score());

        // tính điểm trung binh
        if(saveScore.getTest_score() != null){
            float average = 0;
            float score_oval = 0;
            float score_15 = 0;
            float score_1 = 0;
            if(scoreDto.getOval_score() != null){
                score_oval = scoreDto.getOval_score();
            }
            if(scoreDto.getScore_15() != null){
                score_15 = scoreDto.getScore_15();
            }
            if(scoreDto.getScore_1_period() != null){
                score_1 = scoreDto.getScore_1_period();
            }
            average = (float) (score_oval * 0.05 + score_15 * 0.1 + score_1 * 0.25 + scoreDto.getTest_score() * 0.6);

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            saveScore.setMedium_score(Float.parseFloat(decimalFormat.format(average)));
        }
        if(saveScore.getTest_score() == null){
            saveScore.setMedium_score(null);
        }
        scoreRepository.save(saveScore);
        return true;
    }

    @Override
    public Boolean save(Score score) {
        scoreRepository.save(score);
        return true;
    }

    @Override
    public void delete(Score score) {
        scoreRepository.delete(score);
    }

    // tránh lõi phụ thuộc vòng nên khai báo thêm ở đây
    public Teacher_class findByIdTeacher_class(int id) {
        Optional<Teacher_class> result = teacherClassRepository.findById(id);
        Teacher_class teacher_class = null;
        if(result.isPresent()){
            teacher_class = result.get();
        }
        return teacher_class;
    }


    public Teacher_classDto findByIdDtoTeacher_class(int id) {
        Teacher_classDto teacher_classDto = null;
        Teacher_class teacher_class = findByIdTeacher_class(id);
        if(teacher_class != null){
            teacher_classDto = modelMapper.map(teacher_class,Teacher_classDto.class);
        }
        return teacher_classDto;
    }

}
