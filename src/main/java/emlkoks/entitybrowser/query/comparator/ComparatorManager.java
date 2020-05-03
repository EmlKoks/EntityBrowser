package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.expression.Expression;

import java.lang.reflect.Field;
import javax.persistence.EmbeddedId;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by EmlKoks on 15.06.19.
 */
@Slf4j
public class ComparatorManager {

    public static Expression[] getExpressionByField(Field field) throws ComparatorNotFoundException {
        if (field.getAnnotation(OneToMany.class) != null) {
//            log.info("OneToMany"); //TODO find by entity
//            throw new ComparatorNotFoundException("Not found comparator for OneToMany field "
//                    + field.getName() + "@" + field.getType().getSimpleName());
            return null;
        } else if (field.getAnnotation(ManyToOne.class) != null) {
//            log.info("ManyToOne"); //TODO find by entity
//            throw new ComparatorNotFoundException("Not found comparator for ManyToOne field "
//                    + field.getName() + "@" + field.getType().getSimpleName());
            return null;
        } else if (field.getAnnotation(OneToOne.class) != null) {
//            log.info("OneToOne"); //TODO find by entity
//            throw new ComparatorNotFoundException("Not found comparator for OneToOne field "
//                    + field.getName() + "@" + field.getType().getSimpleName());
            return null;
        } else if (field.getAnnotation(Lob.class) != null) {
//            log.info("Lob"); //TODO find by entity
//            throw new ComparatorNotFoundException("Not found comparator for Lob field "
//                    + field.getName() + "@" + field.getType().getSimpleName());
            return null;
        } else if (field.getAnnotation(EmbeddedId.class) != null) {
//            log.info("Lob"); //TODO skip??
//            throw new ComparatorNotFoundException("Not found comparator for Lob field "
//                    + field.getName() + "@" + field.getType().getSimpleName());
            return null;
        }

        ComparatorFactory comparatorFactory = new ComparatorFactory();
        AbstractComparator<?> abstractComparator = comparatorFactory.getComparator(field.getType());
        if (abstractComparator == null) {
            log.info("Cannot find comparator by class " + field.getType());
            return null;//todo throw exception to show warning
        }
        if (abstractComparator instanceof CharacterComparator) {
            log.info("######## " + field.getDeclaringClass() + " " + field.getName());
        }
        return abstractComparator.getExpressions();
    }
}
