package com.disciplineapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
@Data
@EqualsAndHashCode(exclude = {"taskUser"})
@ToString(exclude={"taskUser"})
@Entity
@Table(name = "task")
public class Task implements Serializable /* extends Activity doesn't work - can't generate self link HAL */ {
    private static final long serialVersionUID = -7737323288628958939L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    private String name;

    private int difficulty;

    private boolean isCompleted;

    private String description;

    @Column(name = "created_when")
    @Type(type = "timestamp")
    private Date createdWhen;

    @Column(name = "remind_date")
    private Date remindDate;

    @Column(name = "time_date")
    @Type(type = "timestamp")
    private Date timeDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User taskUser;

    @JsonIgnore
    @OneToMany(mappedBy = "taskForHistoryTask", fetch = FetchType.LAZY)
    private Set<HistoryTask> historyTasks = new HashSet<>();

    private @Version @JsonIgnore
    Long version;
}
