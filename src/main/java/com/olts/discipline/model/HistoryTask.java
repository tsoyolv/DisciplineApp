package com.olts.discipline.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * o.tsoy
 * 27.04.2017
 */
@Entity
@Table(name = "history_task")
public class HistoryTask extends Task implements Serializable{
    private static final long serialVersionUID = -3355341692863625753L;
    

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", nullable = false)
    private Task taskForHistoryTask;


    public Task getTaskForHistoryTask() {
        return taskForHistoryTask;
    }

    public void setTaskForHistoryTask(Task taskForHistoryTask) {
        this.taskForHistoryTask = taskForHistoryTask;
    }
}
