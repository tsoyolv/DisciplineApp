package com.olts.discipline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * o.tsoy
 * 25.04.2017
 */
@Data
@Entity
@Table(name = "habit")
public class Habit implements Serializable {
    private static final long serialVersionUID = 1033348678616001496L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int difficulty;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User habitUser;

    @Column(name = "created_when")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdWhen;

    @Column(name = "updated_when")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedWhen;

    private String description;

    private @Version @JsonIgnore Long version;

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", difficulty=" + difficulty +
                ", userId=" + habitUser +
                ", createdWhen=" + createdWhen +
                ", description='" + description + '\'' +
                "," +
                '}';
    }
}
