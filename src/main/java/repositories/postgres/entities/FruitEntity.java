package repositories.postgres.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FruitEntity {
    @Id
    public UUID id;
    public String name;
    public String description;

    public FruitEntity() {
    }

    public FruitEntity(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
