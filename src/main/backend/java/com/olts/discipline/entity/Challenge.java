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
import java.util.stream.Collectors;

/**
 * OLTS on 24.08.2017.
 */

@Data
@EqualsAndHashCode(exclude = {"users", "createdBy", "groups"})
@ToString(of = {"id", "name"})
@Entity
@Table(name = "challenge")
public class Challenge implements Serializable {
    private static final long serialVersionUID = -6674807502021869989L;

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

    @Enumerated(EnumType.STRING)
    private ChallengeType type;

    @Enumerated(EnumType.STRING)
    private ChallengeSphere sphere;

    @JsonIgnore
    @ManyToMany(mappedBy = "votedChallenges")
    private Set<User> votedUsers = new HashSet<>();

    @Transient @JsonIgnore
    private Set<Long> votedUsersIds = new HashSet<>();
    @JsonIgnore
    @ManyToMany(mappedBy = "acceptedChallenges")
    private Set<User> acceptedUsers = new HashSet<>();
    @Transient @JsonIgnore
    private Set<Long> acceptedUsersIds = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "messageChallenge")
    private Set<Message> messages = new HashSet<>();

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

    public void addVotedUser(User user) {
        votedUsers.add(user);
        user.getVotedChallenges().add(this);
    }

    public void addAcceptedUser(User user) {
        acceptedUsers.add(user);
        user.getAcceptedChallenges().add(this);
    }

    @PostLoad
    private void postLoad() {
        acceptedUsersIds = acceptedUsers.stream().map(User::getId).collect(Collectors.toSet());
        votedUsersIds = votedUsers.stream().map(User::getId).collect(Collectors.toSet());
    }
}
