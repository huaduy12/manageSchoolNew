package com.example.school.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "score")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score implements Serializable {

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

    @Column(name = "oval_score")
    private Float oval_score;

    @Column(name = "score_15")
    private Float score_15;

    @Column(name = "score_1_period")
    private Float score_1_period;

    @Column(name = "test_score")
    private Float test_score;

    @Column(name = "medium_score")
    private Float medium_score;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "student_id")
    private Student student;

    @CreationTimestamp
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", school_year=" + school_year.getName() +
                ", semester=" + semester.getName() +
                ", oval_score=" + oval_score +
                ", score_15=" + score_15 +
                ", score_1_period=" + score_1_period +
                ", test_score=" + test_score +
                ", medium_score=" + medium_score +
                ", subject=" + subject.getName() +
                ", student=" + student.getFullName() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
