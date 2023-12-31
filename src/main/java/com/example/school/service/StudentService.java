package com.example.school.service;

import com.example.school.dto.StudentDto;
import com.example.school.entity.Parent;
import com.example.school.entity.Student;
import com.example.school.entity.StudentCard;
import com.example.school.entity.Teacher;
import com.example.school.form.student.FormProfileStudent;
import com.example.school.form.student.FormStudent;
import com.example.school.repository.StudentRepository;
import com.example.school.security.EntityUserDetail;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {

    public List<StudentDto> getAllStudent();
    public List<StudentDto> getStudentsStudying();

    public StudentDto getStudentLogin(@AuthenticationPrincipal EntityUserDetail entityUserDetail);

    public Student save(FormStudent formStudent);
    public void save(Student student);
    public void saveProfileStudent(FormProfileStudent formProfileStudent);

    public int countStudentByClass(int class_id);
    public Boolean delete(int id);
    public Student findById(int id);
    public StudentDto findByIdDto(int id);

    public String findFullnameById(int id);
    public StudentDto findByUser_id(int id);
    public Boolean restStudent(int id);
    public Boolean comebackStudent(int id);


    // parent

    public Parent getParentById(int id);

    public Boolean saveParent(int idStudent,Parent parent);

    // student card

    public StudentCard getStudentCardByStudentId(int idStudent);
    public StudentCard saveStudentCard(StudentCard studentCard, MultipartFile multipartFile);

    // lấy students từ classroom
    public List<StudentDto> getStudentByClassIdDto(int classId);
    public List<Student> getStudentByClassId(int classId);

    public Page<StudentDto> findPaginated(int pageNo,int pageSize,String keyword);
}
