package com.olts.discipline.model;

import lombok.Data;

import javax.persistence.*;

/**
 * o.tsoy
 * 27.04.2017
 */
@Data
@Entity
@Table(name = "history_task")
public class HistoryTask extends Task {
    private static final long serialVersionUID = -3355341692863625753L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", nullable = false)
    private Task taskForHistoryTask;
}
