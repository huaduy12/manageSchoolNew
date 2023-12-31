package com.example.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "revenue_class")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "revenue_id")
    private Revenue revenue;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @Override
    public String toString() {
        return "RevenueClass{" +
                "id=" + id +
                ", revenue=" + +revenue.getId()+ ", " +revenue.getName() +
                ", classroom=" + classroom.getId() +", "+ classroom.getName() +
                '}';
    }
}
