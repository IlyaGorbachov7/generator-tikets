package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@jakarta.persistence.Entity(name = "headDepartment")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "department_id"})})
public class HeadDepartment extends Entity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
