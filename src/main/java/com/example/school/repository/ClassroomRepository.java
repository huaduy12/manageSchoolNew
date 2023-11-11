package com.example.school.repository;

import com.example.school.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository  extends JpaRepository<Classroom,Integer> {

    public List<Classroom> findByStatusEquals(boolean status);
}
