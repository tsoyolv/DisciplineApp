package com.olts.discipline.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * OLTS on 24.08.2017.
 */

@Data
@EqualsAndHashCode(exclude = {"users", "createdBy", "groups"})
@ToString(exclude={"id", "users", "createdBy", "groups"})
@Entity
@Table(name = "challenge")
public class Challenge implements Serializable {
    private static final long serialVersionUID = 3836314309380176978L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String name;
    
    private int difficulty;

    /**
     * Challenge completed if accepted users count = level */
    private boolean completed;

    private String description;

    @Column(name = "challenge_date")
    private Date challengeDate;

    private int votes = 0;

    private int acceptedCount;

    private int completedCount;

    /** if 'true' - creator can reject user challenge */
    private boolean withCreator = true;

    private boolean forAllUsers = true;

    @JsonIgnore
    @ManyToMany(mappedBy = "availableChallenges")
    private List<User> users = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "availableChallenges")
    private List<Group> groups = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_when")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdWhen;

    @Column(name = "updated_when")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedWhen;

    private @Version @JsonIgnore
    Long version;
}
