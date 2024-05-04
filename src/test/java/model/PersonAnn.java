package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PersonAnn extends Person {
    @Id
    @Column(name = "primary_key", nullable = false)
    private Long id;

    @Column
    private String name;

    private String age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
