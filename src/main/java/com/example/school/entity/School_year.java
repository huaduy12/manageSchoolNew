package com.example.school.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "school_year")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class School_year implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "school_year",fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE})
    private List<Teacher_class> teacher_class;

    @OneToMany(mappedBy = "school_year",fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE})
    private List<Score> results ;

    @OneToMany(mappedBy = "school_year",fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE})
    private List<Revenue> revenue ;

    @Override
    public String toString() {
        return "School_year{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
