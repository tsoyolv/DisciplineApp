package com.olts.discipline.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * OLTS on 22.09.2017.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id"})
@Entity
@Table(name = "messages")
public class Message implements Serializable {
    private static final long serialVersionUID = -4036936102105651787L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String message;

    @Column(name = "was_sent")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date wasSent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User messageUser;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge messageChallenge;
}
