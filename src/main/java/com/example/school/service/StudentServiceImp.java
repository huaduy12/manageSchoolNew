package com.example.school.service;

import com.example.school.configuration.Pagination;
import com.example.school.dto.StudentDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.*;
import com.example.school.form.student.FormProfileStudent;
import com.example.school.form.student.FormStudent;
import com.example.school.repository.ParentRepository;
import com.example.school.repository.StudentCardRepository;
import com.example.school.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImp implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private StudentCardRepository studentCardRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClassroomService classroomService;
    @Override
    public List<StudentDto> getAllStudent() {
        List<Student> students = studentRepository.findAll();
        List<StudentDto> studentsDto = null;
        if(students != null){
            Type listType =  new TypeToken<List<StudentDto>>() {}.getType ();
            studentsDto = modelMapper.map(students,listType);
        }

        return studentsDto;
    }

    @Override
    public List<StudentDto> getStudentsStudying() {
        return null;
    }

    @Override
    public Student save(FormStudent formStudent) {
        Student student = null;
        Student CopyStudent = findById(formStudent.getId());
        Classroom classroom = classroomService.findById(formStudent.getClassRoomId());
        formStudent.setClassroom(classroom);

        if(CopyStudent != null){
            formStudent.setUser(CopyStudent.getUser());
            formStudent.setParent(CopyStudent.getParent());
        }
        if(formStudent != null){
            student = modelMapper.map(formStudent,Student.class);
        }
        if(CopyStudent != null){
            student.setCreatedAt(CopyStudent.getCreatedAt());
        }
        if(student != null){
            student.setStatus(true);
            return studentRepository.save(student);
        }
        return null;
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void saveProfileStudent(FormProfileStudent formProfileStudent) {
        Student student = findById(formProfileStudent.getId());
        student.setFullName(formProfileStudent.getFullName());
        student.setBirthday(formProfileStudent.getBirthday());
        student.setPhoneNumber(formProfileStudent.getPhoneNumber());
        student.setAddress(formProfileStudent.getAddress());
        studentRepository.save(student);
    }

    @Override
    public Boolean delete(int id) {
        Student student = findById(id);
        if(student != null){
            studentRepository.delete(student);
            return true;
        }
        return false;
    }

    @Override
    public Student findById(int id) {
        Optional<Student> result = studentRepository.findById(id);
        Student student = null;
        if(result.isPresent()){
            student = result.get();
        }
        return student;
    }

    @Override
    public StudentDto findByIdDto(int id) {
        Student student = findById(id);
        StudentDto studentDto = null;
        if(student != null){
           studentDto = modelMapper.map(student,StudentDto.class);
        }
        return studentDto;
    }

    @Override
    public String findFullnameById(int id) {
        String fullName = studentRepository.findFullnameById(id);
        return fullName;
    }

    @Override
    public StudentDto findByUser_id(int id) {
        Student student = studentRepository.findByUser_Id(id);
        StudentDto studentDto =null;
        if(student != null){
            studentDto = modelMapper.map(student,StudentDto.class);
        }
        return studentDto;
    }

    @Override
    public Boolean restStudent(int id) {
        Student student = findById(id);
        if(student != null){
            student.setStatus(false);
            studentRepository.save(student);
            return true;
        }
        return false;
    }

    @Override
    public Boolean comebackStudent(int id) {
        Student student = findById(id);
        if(student != null){
            student.setStatus(true);
            studentRepository.save(student);
            return true;
        }
        return false;
    }

    @Override
    public Parent getParentById(int id) {
        Parent parent = null;
        // tránh trừong hợp nhập id là chữ
       try {
           Optional<Parent> result = parentRepository.findById(id);
           if(result.isPresent()){
               parent = result.get();
           }
       }catch (NumberFormatException e){
           e.printStackTrace();
           return null;
       }
        return parent;
    }

    @Override
    public Boolean saveParent(int idStudent,Parent parent) {

        Student student = findById(idStudent);
        System.out.println(student);
        if(student != null){
            parentRepository.save(parent);
            if(student.getParent() == null){
                Parent parentLast = parentRepository.getLastParent();
                student.setParent(parentLast);
                save(student);
            }
        }
        if(student == null){
            return false;
        }
        return true;
    }

    @Override
    public StudentCard getStudentCardByStudentId(int idStudent) {
        Student student = findById(idStudent);
        StudentCard studentCard = studentCardRepository.findByStudent(student);
        return studentCard;
    }

    @Override
    @Transactional
    public StudentCard saveStudentCard(StudentCard studentCard, String linkImage) {
        Optional<StudentCard> result = studentCardRepository.findById(studentCard.getId());
           StudentCard stUpdate = null;
           StudentCard stCreateNew = new StudentCard();
           StudentCard studentSave = null;

           if(result.isPresent()){
               stUpdate =result.get();
           }
           // nếu ảnh được update
           if(stUpdate != null){
               stUpdate.setPhoto(linkImage);
               studentSave =  studentCardRepository.save(stUpdate);
           }

           // nếu ảnh chưa được tạo, thì tạo mới
           if(stUpdate == null){
               stCreateNew.setPhoto(linkImage);
               stCreateNew.setStudent(studentCard.getStudent());
               studentSave =  studentCardRepository.save(stCreateNew);
           }
        return studentSave;
    }

    @Override
    public List<StudentDto> getStudentByClassIdDto(int classId) {
        List<Student> students = studentRepository.findAllByClassroom_Id(classId);
        List<StudentDto> studentDtos = null;
        if(students != null){
            Type listType =  new TypeToken<List<StudentDto>>() {}.getType ();
            studentDtos = modelMapper.map(students,listType);
        }
        return studentDtos;
    }

    @Override
    public List<Student> getStudentByClassId(int classId) {
        List<Student> students = studentRepository.findAllByClassroom_Id(classId);
        return students;
    }
    @Override
    public Page<StudentDto> findPaginated(int pageNo,int pageSize,String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        if(keyword == null) keyword = " ";
        Page<Student> students = studentRepository.findAll(keyword,pageable);
        List<StudentDto> studentsDto = null;
        if(students != null){
            Type listType =  new TypeToken<List<StudentDto>>() {}.getType ();
            studentsDto = modelMapper.map(students.getContent(),listType);
        return new PageImpl<>(studentsDto,pageable,students.getTotalElements());
        }

        return null;
    }
}
