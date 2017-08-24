package com.olts.discipline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * OLTS on 24.08.2017.
 */

@Data
@EqualsAndHashCode(exclude = {"challengeUser", "challengeFrom"})
@ToString(exclude={"id", "challengeUser", "challengeFrom"})
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

    private boolean isCompleted;

    private String description;

    @Column(name = "challenge_date")
    private Date challengeDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User challengeUser;

    @ManyToOne
    @JoinColumn(name = "challenge_from", nullable = false)
    private User challengeFrom;

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
