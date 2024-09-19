package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created entities is mapping table view
 */
@Getter
@Setter
@jakarta.persistence.Entity(name = "department")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","faculty_id"})})
public class Department extends Entity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Specialization> specializations = new ArrayList<>();
}
