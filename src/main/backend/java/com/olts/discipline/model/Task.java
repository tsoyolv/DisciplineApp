package com.olts.discipline.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * o.tsoy
 * 27.04.2017
 */
@Entity
@Table(name = "task")
@Inheritance(strategy=InheritanceType.JOINED)
public class Task implements Serializable {
    private static final long serialVersionUID = -7737323288628958939L;

    @Id
    private String id;

    private String name;

    private int difficulty;

    @Column(name = "created_when")
    @Type(type = "timestamp")
    private Date createdWhen;

    private String description;

    @Column(name = "remind_date")
    private Date remindDate;

    @Column(name = "time_date")
    @Type(type = "timestamp")
    private Date timeDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User taskUser;

    @OneToMany(mappedBy = "taskForHistoryTask", fetch = FetchType.LAZY)
    private Set<HistoryTask> historyTasks = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public Date getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(Date timeDate) {
        this.timeDate = timeDate;
    }

    public User getTaskUser() {
        return taskUser;
    }

    public void setTaskUser(User taskUser) {
        this.taskUser = taskUser;
    }

    public Set<HistoryTask> getHistoryTasks() {
        return historyTasks;
    }

    public void setHistoryTasks(Set<HistoryTask> historyTasks) {
        this.historyTasks = historyTasks;
    }
}
