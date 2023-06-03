package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Created entities is mapping table view
 */
@Getter
@Setter
@jakarta.persistence.Entity(name = "department")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","faculty_id"})})
public class Department extends Entity{
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Specialization> specializations;
}
