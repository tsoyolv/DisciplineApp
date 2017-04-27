package com.olts.discipline.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * o.tsoy
 * 25.04.2017
 */
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
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public User getHabitUser() {
        return habitUser;
    }

    public void setHabitUser(User habitUser) {
        this.habitUser = habitUser;
    }

    public Date getCreatedWhen() {
        return createdWhen;
    }

    public void setCreatedWhen(Date createdWhen) {
        this.createdWhen = createdWhen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
