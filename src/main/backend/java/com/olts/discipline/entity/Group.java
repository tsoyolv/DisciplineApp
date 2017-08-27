package com.olts.discipline.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * OLTS on 27.08.2017.
 */
@Data
//@EqualsAndHashCode(exclude = {"challengeUser", "challengeFrom"})
@ToString(exclude={"id"})
@Entity
@Table(name = "group")
public class Group implements Serializable {
    private static final long serialVersionUID = -3988269290702387143L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "group_user", joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users;
}
