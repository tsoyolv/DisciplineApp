package com.olts.discipline.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * OLTS on 03.09.2017.
 */
@Data
/*@EqualsAndHashCode(exclude = {"habitUser"})
@ToString(exclude={"id", "habitUser"})*/
@Entity
@Table(name = "activity_sphere")
public class ActivitySphere implements Serializable {
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
