package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Created entities is mapping table view
 */
@Getter
@Setter
@jakarta.persistence.Entity(name = "specialization")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "department_id"})})
public class Specialization extends Entity {
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "specialization", fetch = FetchType.LAZY)
    private List<Discipline> disciplines;
}
