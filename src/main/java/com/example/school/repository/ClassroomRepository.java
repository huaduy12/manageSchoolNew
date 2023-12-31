package com.example.school.repository;

import com.example.school.entity.Classroom;
import com.example.school.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository  extends JpaRepository<Classroom,Integer> {

    public List<Classroom> findByStatusEquals(boolean status);

    List<Classroom> findByIdNotInAndStatus(List<Integer> id,boolean status);

    @Query("select t from Classroom t where (concat(t.name,' ',t.academic_year) like %?1%)")
    Page<Classroom> findAll(String keyword, Pageable pageable);


}
