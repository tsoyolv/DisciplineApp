package org.discipline.entity.habit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.discipline.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * What the user can develop.
 */
@Data
@EqualsAndHashCode(exclude = {"habitUser", "histories", "activitySphere"})
@ToString(exclude = {"id", "habitUser", "histories", "activitySphere"})
@Entity
@Table(name = "habit")
public class Habit implements Serializable /* extends Activity doesn't work - can't generate self link HAL */ {
    private static final long serialVersionUID = 1033348678616001496L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "Habit name must be not empty")
    private String name;

    @NotNull(message = "Habit difficulty must be not empty")
    @Range(min = 1)
    private int difficulty;

    private boolean completed;

    private String description;

    private boolean achieved;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User habitUser;

    @Column(name = "created_when")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdWhen;

    @Column(name = "updated_when")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedWhen;

    @Column(name = "completed_count")
    private int completedCount;

    @Column(name = "non_completed_count")
    private int nonCompletedCount;

    @JsonIgnore
    @OneToOne(mappedBy = "habit")
    private ActivitySphere activitySphere;

    @JsonIgnore
    @OneToMany(mappedBy = "originalHabit")
    private Set<HabitHistory> histories = new HashSet<>();

    private @Version
    @JsonIgnore
    Long version;
}
