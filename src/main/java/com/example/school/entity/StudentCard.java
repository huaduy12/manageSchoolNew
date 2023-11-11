package com.example.school.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "student_card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "photo")
    private String photo;


    @OneToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "student_id")
    private Student student;


    @Override
    public String toString() {
        return "StudentCard{" +
                "id=" + id +
                ", photo='" + photo + '\'' +
                "student" + student.getId()+
                '}';
    }
}
