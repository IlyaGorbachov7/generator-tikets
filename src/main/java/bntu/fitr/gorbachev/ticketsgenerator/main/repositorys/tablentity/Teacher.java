package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Created entities is mapping table view
 * <p>
 * it is necessary to make sure that uniqueness is checked by to fields !!!
 */
@Getter
@Setter
@jakarta.persistence.Entity(name = "teacher")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "faculty_id"})})
public class Teacher extends Entity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
}
