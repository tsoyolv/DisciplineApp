package com.disciplineapp.entity.habit;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Sphere of the life where user can develop him habits.
 */
@Data
/*@EqualsAndHashCode(exclude = {"habitUser"})
@ToString(exclude={"id", "habitUser"})*/
@Entity
@Table(name = "activity_sphere")
class ActivitySphere implements Serializable {
    private static final long serialVersionUID = -2133500032462136937L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String type;// todo mental, intellectual, physical

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;
}
