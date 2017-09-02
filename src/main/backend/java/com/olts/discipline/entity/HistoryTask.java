package com.olts.discipline.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * o.tsoy
 * 27.04.2017
 */
@Data
@Entity
@Table(name = "history_task")
public class HistoryTask implements Serializable /*extends Task*/ {
    private static final long serialVersionUID = -3355341692863625753L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    private String name;

    private int difficulty;

    private boolean isCompleted;

    private String description;

    private @Version @JsonIgnore
    Long version;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", nullable = false)
    private Task taskForHistoryTask;
}
