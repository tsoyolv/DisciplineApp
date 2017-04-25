package com.olts.discipline.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * o.tsoy
 * 25.04.2017
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 2082395294433209579L;

    @Column
    @Id
    private long id;

    @Column(name = "created_when")
    @Type(type = "timestamp")
    private Date createdWhen;

    private String login;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private Integer taskScore;
    private Integer habitScore;
    private Integer score;

    @OneToMany(mappedBy = "user")
    private Set<Habit> habits = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedWhen() {
        return createdWhen;
    }

    public void setCreatedWhen(Date createdWhen) {
        this.createdWhen = createdWhen;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTaskScore() {
        return taskScore;
    }

    public void setTaskScore(int taskScore) {
        this.taskScore = taskScore;
    }

    public int getHabitScore() {
        return habitScore;
    }

    public void setHabitScore(int habitScore) {
        this.habitScore = habitScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Set<Habit> getHabits() {
        return habits;
    }

    public void setHabits(Set<Habit> habits) {
        this.habits = habits;
    }
}
