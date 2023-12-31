package com.example.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "revenue_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Revenue_Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @CreationTimestamp
    @Column(name = "payment_time",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date payment_time;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "revenue_id")
    private Revenue revenue;


    @Override
    public String toString() {
        return "Revenue_Detail{" +
                "id=" + id +
                ", payment_time=" + payment_time +
                ", student=" + student.getFullName() +
                ", revenue=" + revenue.getName() +
                '}';
    }
}
