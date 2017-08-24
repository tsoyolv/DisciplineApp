package com.olts.discipline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * OLTS on 20.08.2017.
 */
@Data
@EqualsAndHashCode()
@ToString(exclude={"id"})
@Entity
@Table(name = "habit_history")
public class HabitHistory implements Serializable /* extends Activity doesn't work - can't generate self link HAL  */ {
    private static final long serialVersionUID = 386894449287914604L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int difficulty;

    private boolean isCompleted;

    private String description;

    @ManyToOne
    @JoinColumn(name = "original_habit", nullable = false)
    private Habit originalHabit;

    @Column(name = "was_completed")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date wasCompleted;

    @Column(name = "completed_count")
    private int completedCount;

    private @Version @JsonIgnore
    Long version;
}
