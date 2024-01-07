package com.example.school.service;

import com.example.school.dto.*;
import com.example.school.entity.*;
import com.example.school.form.classroom.FormSubjectClass;
import com.example.school.repository.TeacherClassRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.bytebuddy.description.method.MethodDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherClassServiceImp implements TeacherClassService{

    @Autowired
    private TeacherClassRepository teacherClassRepository;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private YearSemesterService yearSemesterService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Teacher_class findById(int id) {
        Optional<Teacher_class> result = teacherClassRepository.findById(id);
        Teacher_class teacher_class = null;
        if(result.isPresent()){
            teacher_class = result.get();
        }
        return teacher_class;
    }

    @Override
    public Teacher_classDto findByIdDto(int id) {
        Teacher_classDto teacher_classDto = null;
        Teacher_class teacher_class = findById(id);
        if(teacher_class != null){
            teacher_classDto = modelMapper.map(teacher_class,Teacher_classDto.class);
        }
        return teacher_classDto;
    }

    @Override
    public Teacher_class getByIdRoleAndClass_id(int role, int class_id) {
        Teacher_class teacher_class = teacherClassRepository.findByRoleEqualsAndClassroom_Id(role,class_id);
        return teacher_class;
    }

    @Override
    public List<Teacher_classDto> getByClass_id(int class_id) {
        List<Teacher_class> teacher_classes = teacherClassRepository.findByClassroom_IdAndSubjectNotNull(class_id);
        List<Teacher_classDto> teacher_classDtos = null;
        if(teacher_classes != null){
            Type type = new TypeToken<List<Teacher_classDto>>(){}.getType();
            teacher_classDtos = modelMapper.map(teacher_classes,type);
        }
        return teacher_classDtos;
    }

    @Override
    public Page<Teacher_classDto> getByTeacher_idAndSubjectNotNull(int teacher_id,int pageNo,int pageSize,String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        if(keyword == null) keyword = " ";
        Page<Teacher_class> teacher_classes = teacherClassRepository.findByTeacher_IdAndSubjectNotNull(teacher_id,keyword,pageable);
        List<Teacher_classDto> teacher_classDtos = null;
        if(teacher_classes != null){
            Type type = new TypeToken<List<Teacher_classDto>>() {}.getType();
            teacher_classDtos = modelMapper.map(teacher_classes.getContent(),type);
        }
        return new PageImpl<>(teacher_classDtos,pageable,teacher_classes.getTotalElements());
    }

    @Override
    public List<Teacher_class> getByTeacher_id(int teacher_id) {
        List<Teacher_class> teacher_classes = teacherClassRepository.findByTeacher_Id(teacher_id);
        return teacher_classes;
    }

    @Override
    public Page<ClassroomDto> getByIdRoleAndTeacher_id(int role, int teacher_id,int pageNo,int pageSize,String keyword) {

        //List<Teacher_class> teacher_classes = teacherClassRepository.findByRoleEqualsAndTeacher_Id(role,teacher_id);
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        if(keyword == null) keyword = " ";
        Page<Teacher_class> teacher_classes = teacherClassRepository.findTeacherHomeroom
                (role,teacher_id,keyword,pageable);

        List<ClassroomDto> classroomDtos = new ArrayList<>();
        for (Teacher_class t:teacher_classes.getContent()) {
            ClassroomDto classroomDto = modelMapper.map(t.getClassroom(),ClassroomDto.class);
            classroomDtos.add(classroomDto);
        }
        return new PageImpl<>(classroomDtos,pageable,teacher_classes.getTotalElements());
    }

    @Override
    public List<Teacher_class> getClassroomByTeacher(int role,int teacher_id){
        List<Teacher_class> teacher_classes = teacherClassRepository.findByRoleEqualsAndTeacher_Id(role,teacher_id);

        return teacher_classes;
    }

    @Override
    public boolean isExistTeacher_class(int year_id, int semester_id, int class_id, int subject_id) {
        Teacher_class teacher_class = teacherClassRepository.findByAssign(year_id,semester_id,class_id,subject_id);
        return teacher_class != null ? true : false;
    }


    @Override
    public Boolean save(Teacher_class teacher_class, int id_class) {

        if(teacher_class != null){
            Classroom classroom = classroomService.findById(id_class);
            teacher_class.setClassroom(classroom);
            teacher_class.setStatus(true);
            teacherClassRepository.save(teacher_class);
            return true;
        }
        return false;
    }

    @Override
    public Boolean saveSubjectClass(FormSubjectClass formSubjectClass) {
        Teacher_class teacher_classEdit = findById(formSubjectClass.getId());

        School_year school_year= yearSemesterService.findByIdSchool(formSubjectClass.getSchool_yearId());
        Semester semester = yearSemesterService.findByIdSemester(formSubjectClass.getSemesterId());
        Teacher teacher = teacherService.findById(formSubjectClass.getTeacherId());
        Subject subject = subjectService.findById(formSubjectClass.getSubjectId());
        Classroom classroom = classroomService.findById(formSubjectClass.getClassroomId());

        // nếu đã được phân công rồi thì return luôn
        if(isExistTeacher_class(school_year.getId(),semester.getId(),classroom.getId(),subject.getId())){
            return false;
        }
         // nếu chưa tồn tại thì thực hiện việc song song tao bảng điểm và thêm teacher_class;
        else if(teacher_classEdit == null && !isExistTeacher_class(school_year.getId(),semester.getId(),classroom.getId(),subject.getId())){
            Teacher_class teacher_class = new Teacher_class();
            teacher_class.setRole(1);
            teacher_class.setSchool_year(school_year);
            teacher_class.setSemester(semester);
            teacher_class.setTeacher(teacher);
            teacher_class.setSubject(subject);
            teacher_class.setClassroom(classroom);
            teacher_class.setStatus(true);
            teacherClassRepository.save(teacher_class);

            // lấy tất cả học sinh của lớp đó
            List<Student> students = studentService.getStudentByClassId(formSubjectClass.getClassroomId());
            Score score = null;
            for (Student student : students){
                score = new Score();
                score.setSchool_year(school_year);
                score.setSemester(semester);
                score.setSubject(subject);
                score.setStudent(student);
                scoreService.save(score);
            }

        }
        if(teacher_classEdit != null){
            teacher_classEdit.setSchool_year(school_year);
            teacher_classEdit.setSemester(semester);
            teacher_classEdit.setTeacher(teacher);
            teacher_classEdit.setSubject(subject);
            teacherClassRepository.save(teacher_classEdit);
        }
        return  true;
    }

    @Override
    public Boolean deleteSubjectClass(int id) {
        Teacher_class teacher_class = findById(id);
        if (teacher_class != null){
            // lấy những học sinh học môn học muốn xóa
            List<Student> students = studentService.getStudentByClassId(teacher_class.getClassroom().getId());
            Subject subject = subjectService.findById(teacher_class.getSubject().getId());
            int[] flagScore = new int[4];
             // từ học sinh và môn học lấy ra các scores
            for(Student student:students){
                Score score = scoreService.findByStudentIdAndSubjectId(teacher_class.getSchool_year().getId(),
                         teacher_class.getSemester().getId(),student.getId(),teacher_class.getSubject().getId());
                if(score.getOval_score() != null){
                    flagScore[0]++;
                }
                if(score.getScore_15() != null){
                    flagScore[1]++;
                }
                if(score.getScore_1_period() != null){
                    flagScore[2]++;
                }
                if(score.getTest_score() != null){
                    flagScore[3]++;
                }
            }
            //System.out.println("Điểm: "+flagScore[0] + " " + flagScore[1] +" "+ flagScore[2] +" "+ flagScore[3]);
            // có nghĩa điểm đã được nhập nên không xóa được
            if(flagScore[0] > 0 || flagScore[1] > 0 || flagScore[2] > 0 || flagScore[3] > 0){
                return false;
            }else {
                // còn không thi xóa bình thường
                for(Student student:students){
                    Score score = scoreService.findByStudentIdAndSubjectId(teacher_class.getSchool_year().getId(),
                            teacher_class.getSemester().getId(),student.getId(),teacher_class.getSubject().getId());
                    scoreService.delete(score);
                }
                teacherClassRepository.deleteById(id);
                return true;
            }

        }
        return false;

    }


}
