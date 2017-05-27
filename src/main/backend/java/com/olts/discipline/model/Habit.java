package com.olts.discipline.model;

import lombok.Data;
import org.hibernate.annotations.Type;

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
    private long id;

    private String name;

    private int difficulty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User habitUser;

    @Column(name = "created_when")
    @Type(type="timestamp")
    private Date createdWhen;

    private String description;

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
    }}
