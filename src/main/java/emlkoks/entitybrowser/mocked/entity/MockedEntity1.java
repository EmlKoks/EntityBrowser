package emlkoks.entitybrowser.mocked.entity;

import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


@Data
@AllArgsConstructor
public class MockedEntity1 {
    @Id
    Integer id;
    String stringField;
    Integer integerField;
    Double doubleField;
    Boolean booleanField;
    boolean boolField;
    Date dateField;

    @Enumerated
    ExampleEnum enumField;
}
