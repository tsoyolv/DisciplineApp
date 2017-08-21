package com.olts.discipline.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * OLTS on 20.08.2017.
 */
@Data
@EqualsAndHashCode()
@ToString(exclude={"id"})
@Entity
@Table(name = "habit_history")
public class HabitHistory extends Activity {
    private static final long serialVersionUID = 386894449287914604L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "original_habit", nullable = false)
    private Habit originalHabit;

    @Column(name = "was_completed")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date wasCompleted;

    @Column(name = "completed_count")
    private int completedCount;
}
