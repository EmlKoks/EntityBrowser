package test;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
public class EntityWithInteger implements IdEntity {
    @Id
    private Long id;
    private Integer testInteger;

    public EntityWithInteger(Integer testInteger) {
        this.testInteger = testInteger;
    }
}
