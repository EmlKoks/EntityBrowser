package emlkoks.entitybrowser.mocked.entity;

import java.util.Date;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MockedEntity2 {
    @Id
    private Integer id;
    private String stringField;
    private Integer integerField;
    private Double doubleField;
    private Boolean booleanField;
    private boolean boolField;
    private Date dateField;

    @Enumerated
    private ExampleEnum enumField;
}
