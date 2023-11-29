package com.example.school.repository;

import com.example.school.entity.Student;
import com.example.school.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Integer> {

    @Query("select t from Subject t where (concat(t.id,' ',t.name) like %?1%)")
    Page<Subject> findAll(String keyword, Pageable pageable);
}
