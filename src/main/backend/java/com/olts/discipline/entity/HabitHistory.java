package com.olts.discipline.entity;

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
@EqualsAndHashCode(exclude = "originalHabit")
@ToString(exclude={"id", "originalHabit"})
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

    private boolean completed;

    private String description;

    @ManyToOne
    @JoinColumn(name = "original_habit", nullable = false)
    private Habit originalHabit;

    @Column(name = "completed_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date completedDate;

    @Column(name = "completed_count")
    private int completedCount;

    @Column(name = "non_completed_count")
    private int nonCompletedCount;

    private @Version @JsonIgnore
    Long version;
}
