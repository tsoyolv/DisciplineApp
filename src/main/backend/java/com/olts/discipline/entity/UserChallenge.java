package com.olts.discipline.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * OLTS on 16.09.2017.
 */
@Data
@EqualsAndHashCode(exclude = {"originalChallenge", "originalChallenge"})
@ToString(exclude={"id", "challengeUser", "originalChallenge"})
@Entity
@Table(name = "user_challenge")
public class UserChallenge implements Serializable {
    private static final long serialVersionUID = 3836314309380176978L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int votes;

    @Column(name = "completed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedDate;

    @Column(name = "accepted_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date acceptedDate;

    @Column(name = "updated_when")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedWhen;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge originalChallenge;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User challengeUser;

    @JsonIgnore
    @ManyToMany(mappedBy = "votedUserChallenges")
    private Set<User> votedUsers = new HashSet<>();

    private @Version
    @JsonIgnore
    Long version;

    public void addVotedUser(User user) {
        votedUsers.add(user);
        user.getVotedUserChallenges().add(this);
    }
}