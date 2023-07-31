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
@jakarta.persistence.Entity(name = "faculty")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "university_id"})})
public class Faculty extends Entity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Department> departments = new ArrayList<>();
}
