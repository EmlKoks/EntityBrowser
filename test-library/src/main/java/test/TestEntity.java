package test;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
public class TestEntity {

    @Id
    private Long id;
    private Boolean testBoolean;

    public TestEntity(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }
}
