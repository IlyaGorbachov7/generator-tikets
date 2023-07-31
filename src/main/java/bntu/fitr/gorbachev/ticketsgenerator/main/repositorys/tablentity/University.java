package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created entities is mapping table view
 */
@Getter
@Setter
@jakarta.persistence.Entity(name = "university")
@Table(name = "univ") // in HQL queries Hibernate automatically replace EntityName to particular TableName of DB
public class University extends Entity{
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Faculty> faculties = new ArrayList<>();
}

