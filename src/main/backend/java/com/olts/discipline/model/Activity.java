package com.olts.discipline.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * OLTS on 20.08.2017.
 */
@Data
@Entity
public class Activity implements Serializable {
    private static final long serialVersionUID = 4706389047942641345L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int difficulty;

    private boolean isCompleted;

    private String description;
}
