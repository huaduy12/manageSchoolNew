package com.example.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "parent")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name_father")
    private String name_father;

    @Column(name = "phonenumber_father")
    private String phonenumber_father;

    @Column(name = "address_father")
    private String address_father;

    @Column(name = "name_mother")
    private String name_mother;

    @Column(name = "phonenumber_mother")
    private String phonenumber_mother;

    @Column(name = "address_mother")
    private String address_mother;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST},mappedBy = "parent")
    private Student student;

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", name_father='" + name_father + '\'' +
                ", phonenumber_father='" + phonenumber_father + '\'' +
                ", address_father='" + address_father + '\'' +
                ", name_mother='" + name_mother + '\'' +
                ", phonenumber_mother='" + phonenumber_mother + '\'' +
                ", address_mother='" + address_mother + '\'' + "student_name" + student.getFullName()+
                '}';
    }
}
