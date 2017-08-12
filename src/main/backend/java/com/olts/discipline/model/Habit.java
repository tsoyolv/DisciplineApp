package com.olts.discipline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

/**
 * o.tsoy
 * 25.04.2017
 */
@Data
@EqualsAndHashCode(exclude = {"habitUser"})
@ToString(exclude={"id", "habitUser"})
@Entity
@Table(name = "habit")
public class Habit implements Serializable {
    private static final long serialVersionUID = 1033348678616001496L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "Habit name must be not empty")
    private String name;

    @NotNull(message = "Habit difficulty must be not empty")
    @Range(min = 1)
    private Integer difficulty;

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

    private String description;

    private @Version @JsonIgnore Long version;
}
