package com.olts.discipline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
public class Task extends Activity {
    private static final long serialVersionUID = -7737323288628958939L;

    private String name;

    private int difficulty;

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
}
