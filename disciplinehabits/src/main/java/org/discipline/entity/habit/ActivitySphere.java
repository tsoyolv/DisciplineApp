package org.discipline.entity.habit;

import lombok.Data;

import javax.persistence.*;
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
