package test;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Setter;

@Entity
@Setter
public class EntityWithCharacter implements IdEntity {
    @Id
    private Long id;
    private Character testCharacter;

    public EntityWithCharacter(Character testCharacter) {
        this.testCharacter = testCharacter;
    }
}
