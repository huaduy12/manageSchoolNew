package com.example.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "revenue")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name",length = 300,nullable = false)
    private String name;

    @Column(name = "description",length = 500)
    private String description;

    @Column(name = "price",nullable = false)
    private long price;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "expired_at",nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expiredAt;

    @Column(name = "status",columnDefinition ="tinyint(1) default 1",nullable = false)
    private int status;

    @OneToMany(mappedBy = "revenue",fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    private List<Revenue_Detail> revenue_details;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "year_id")
    private School_year school_year;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @OneToMany(mappedBy = "revenue",fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    private List<RevenueClass> revenueClasses;

    @Override
    public String toString() {
        return "Revenue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", expiredAt=" + expiredAt +
                ", status=" + status +
                ", school_year=" + school_year.getName() +
                ", semester=" + semester.getName() +

                '}';
    }
}
