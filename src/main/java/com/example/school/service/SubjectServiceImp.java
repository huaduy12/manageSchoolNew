package com.example.school.service;

import com.example.school.dto.StudentDto;
import com.example.school.dto.SubjectDto;
import com.example.school.dto.TeacherDto;
import com.example.school.dto.Teacher_classDto;
import com.example.school.entity.*;
import com.example.school.repository.SubjectRepository;
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
public class SubjectServiceImp implements SubjectService{

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherClassRepository teacherClassRepository;

//    @Autowired
//    private TeacherClassService teacherClassService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SubjectDto> getAllSubject() {
        List<Subject> subjects = subjectRepository.findAll();

        List<SubjectDto> subjectDtos = null;
        if(subjects != null){
            Type type = new TypeToken<List<SubjectDto>>(){}.getType();
            subjectDtos = modelMapper.map(subjects,type);
        }
        return subjectDtos;
    }

    @Override
    public Boolean save(SubjectDto subjectDto) {
        Subject subject =null;
        if(subjectDto != null){
            subjectDto.setStatus(true);
            subject = modelMapper.map(subjectDto,Subject.class);
            subjectRepository.save(subject);
            return true;
        }
        return false;
    }


    @Override
    public Boolean block(int idSubject) {
        Subject subject = findById(idSubject);
        if(subject != null){
            subject.setStatus(false);
            subjectRepository.save(subject);
            return true;
        }
        return false ;
    }

    @Override
    public Boolean open(int idSubject) {
        Subject subject = findById(idSubject);
        if(subject != null){
            subject.setStatus(true);
            subjectRepository.save(subject);
            return true;
        }
        return false ;
    }

    @Override
    public Boolean delete(int idSubject) {
        Subject subject = findById(idSubject);
        if(subject != null){
            subjectRepository.delete(subject);
            return true;
        }
        return false ;
    }

    @Override
    public Subject findById(int id) {
        Optional<Subject> result = subjectRepository.findById(id);
        Subject subject = null;
        if(result.isPresent()){
            subject = result.get();
        }
        return subject;
    }

    @Override
    public SubjectDto findByIdDto(int id) {
        Subject subject = findById(id);
        SubjectDto subjectDto = null;
        if(subject != null){
            subjectDto = modelMapper.map(subject,SubjectDto.class);
        }
        return subjectDto;
    }

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
    public List<Teacher_classDto> getSubjectsByClass_id(int class_id) {
        List<Teacher_classDto> teacher_classes = getByClass_id(class_id);
        return teacher_classes;
    }

    @Override
    public Page<SubjectDto> findPaginated(int pageNo, int pageSize) {

        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Subject> subjects = subjectRepository.findAll(pageable);

        List<SubjectDto> subjectDtos = null;
        if(subjects != null){
            Type listType =  new TypeToken<List<SubjectDto>>() {}.getType ();
            subjectDtos = modelMapper.map(subjects.getContent(),listType);
            return new PageImpl<>(subjectDtos,pageable,subjects.getTotalElements());
        }
        return null;
    }


}
