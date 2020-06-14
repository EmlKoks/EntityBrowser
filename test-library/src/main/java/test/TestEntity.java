package test;

import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
public class TestEntity {

    @Id
    private Long id;
}
