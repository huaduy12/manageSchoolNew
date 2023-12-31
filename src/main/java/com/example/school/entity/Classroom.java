package com.example.school.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "classroom")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Classroom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "academic_year",nullable = false)
    private String academic_year;

    @Column(name = "note")
    private String note;

    @Column(name = "status",columnDefinition = "tinyint(1) default 1")
    private boolean status;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST},mappedBy = "classroom")
    private List<Student> students;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST},mappedBy = "classroom")
    private List<Teacher_class> teacher_classes;

    @OneToMany(mappedBy = "classroom",fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    private List<RevenueClass> revenueClasses;

    @Override
    public String toString() {
        return "Classroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", academic_year='" + academic_year + '\'' +
                ", note='" + note + '\'' +
                " teacherclass= " + teacher_classes.toString()  +
                '}';
    }
}
