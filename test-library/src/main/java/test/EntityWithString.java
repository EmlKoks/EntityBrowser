package test;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Setter;

@Entity
@Setter
public class EntityWithString implements IdEntity {
    @Id
    private Long id;
    private String testString;

    public EntityWithString(String testString) {
        this.testString = testString;
    }
}
