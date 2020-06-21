package test;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Setter;

@Entity
@Setter
public class EntityWithBoolean implements IdEntity {

    @Id
    private Long id;
    private Boolean testBoolean;

    public EntityWithBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }
}
