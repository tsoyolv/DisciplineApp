package com.olts.discipline.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * OLTS on 27.08.2017.
 */
@Data
@EqualsAndHashCode(exclude = {"users", "createdBy", "description"})
@ToString(exclude={"id", "createdBy", "description"})
@Entity(name = "Groups")
@Table(name = "group_tab")
public class Group implements Serializable {
    private static final long serialVersionUID = -3988269290702387143L;

    public Group() {
    }

    // only for tests
    public Group(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String description;

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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "group_user", joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;
}
