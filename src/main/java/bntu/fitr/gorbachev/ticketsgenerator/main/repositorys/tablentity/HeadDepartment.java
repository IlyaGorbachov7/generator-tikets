package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@jakarta.persistence.Entity(name = "headDepartment")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "department_id"})})
public class HeadDepartment extends Entity {
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
}
