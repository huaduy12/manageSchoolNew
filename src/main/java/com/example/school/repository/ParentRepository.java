package com.example.school.repository;

import com.example.school.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent,Integer> {


    @Query("select u from Parent u  where u.id = (select max(id) from Parent )")
    public Parent getLastParent();
}
