package test;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Setter;

@Entity
@Setter
public class EntityWithDate implements IdEntity {
    @Id
    private Long id;
    private LocalDate testDate;

    public EntityWithDate(LocalDate testDate) {
        this.testDate = testDate;
    }
}
