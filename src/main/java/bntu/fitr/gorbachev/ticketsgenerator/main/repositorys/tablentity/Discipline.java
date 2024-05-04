package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;


import jakarta.persistence.*;
import lombok.*;

/**
 * Created entities is mapping table view
 */
@Getter
@Setter
@jakarta.persistence.Entity(name = "discipline")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","specialization_id"})})
public class Discipline extends Entity {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization = new Specialization();
}
