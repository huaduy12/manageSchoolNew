package com.example.school.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Entity
@Table(name = "teacher_class")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher_class implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "year_id")
    @NotNull(message = "Không được để trống")
    private School_year school_year;


    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "semester_id")
    @NotNull(message = "Không được để trống")
    private Semester semester;

    // 0: giáo viên chủ nhiệm,  1: giáo viên bộ môn
    @Column(name = "role")
    private int role;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "teacher_id")
    @NotNull(message = "Không được để trống")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "class_id")
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "status")
    private boolean status;


    @Override
    public String toString() {
        return "Teacher_class{" +
                "id=" + id +
                ", school_year=" + school_year.getName() +
                ", semester=" + semester.getName() +
                ", role=" + role +
                ", teacher=" + teacher.getFullName() +
                ", classroom=" + classroom.getName() +
                ", status=" + status +
                '}';
    }
}
