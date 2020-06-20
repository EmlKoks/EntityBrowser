package test;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Setter;

@Entity
@Setter
public class TestEntity {

    @Id
    private Long id;
}
