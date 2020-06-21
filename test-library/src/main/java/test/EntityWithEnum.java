package test;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Setter;

@Entity
@Setter
public class EntityWithEnum implements IdEntity {
    @Id
    private Long id;
    private TestEnum testEnum;

    public EntityWithEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }
}
