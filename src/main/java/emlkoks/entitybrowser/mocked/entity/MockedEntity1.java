package emlkoks.entitybrowser.mocked.entity;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MockedEntity1 {
    @Id
    Integer id;
    String stringField;
    Integer integerField;
}
