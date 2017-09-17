package com.olts.discipline.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.Session;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.Type;
import org.hibernate.tuple.ValueGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * o.tsoy
 * 25.04.2017
 */
@Data
@EqualsAndHashCode(exclude = {"passwordConfirm", "habits", "tasks", "roles", "availableChallenges"})
@ToString(exclude={"id", "password", "passwordConfirm", "habits", "tasks", "roles", "availableChallenges"})
@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 2082395294433209579L;

    public static final class RankValueGenerator implements ValueGenerator<Integer> {
        @Override
        public Integer generateValue(Session session, Object owner) {
            return 999; // todo
        }
    }

    public User() {}

    // constructor only for tests
    public User(long id, String username, String firstName, Integer level, Integer rank) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.level = level;
        this.rank = rank;
    }

    @Column
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name = "created_when")
    @Type(type = "timestamp")
    private Date createdWhen;

    private String username;

    @JsonIgnore
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "task_score")
    private Integer taskScore = 0;

    @Column(name = "habit_score")
    private Integer habitScore = 0;

    private Integer score = 0; // todo default by DB

    private Boolean hidden = false;

    private Integer level = 1; // todo can't be changed by rest POST, only GET

    @Column(name = "level_percentage")
    private Integer levelPercentage = 1; // can't be changed by rest POST, only GET

    @Column(name = "progress_per_day")
    private Integer progressPerDay = 100;

    @GeneratorType(type = RankValueGenerator.class)
    private Integer rank;

    @Column(name = "birth_date")
    @Type(type = "date")
    private Date birthDate;

    private String city;

    private String country;

    @Column(name = "allowed_groups")
    private Integer allowedGroups = level;

    @Column(name = "allowed_challenges")
    private Integer allowedChallenges = level;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private List<Group> groups = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "habitUser")
    private Set<Habit> habits = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "challengeUser")
    private List<UserChallenge> userChallenges = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "taskUser")
    private Set<Task> tasks = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_challenge", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "challenge_id", referencedColumnName = "id"))
    private List<Challenge> availableChallenges = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Transient
    private String passwordConfirm;
}