package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(doNotUseGetters = true)
@MappedSuperclass
public abstract class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @Column(name = "name", nullable = false)
    private String name;
}
