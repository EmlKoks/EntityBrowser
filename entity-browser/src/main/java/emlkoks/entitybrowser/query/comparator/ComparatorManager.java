package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.expression.Expression;
import emlkoks.entitybrowser.session.entity.FieldProperty;
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

    public static Expression[] getExpressionByField(FieldProperty field) throws ComparatorNotFoundException {
        if (field.hasAnnotation(OneToMany.class)) {
//            log.info("OneToMany"); //TODO find by entity
//            throw new ComparatorNotFoundException("Not found comparator for OneToMany field "
//                    + field.getName() + "@" + field.getType().getSimpleName());
            return null;
        } else if (field.hasAnnotation(ManyToOne.class)) {
//            log.info("ManyToOne"); //TODO find by entity
//            throw new ComparatorNotFoundException("Not found comparator for ManyToOne field "
//                    + field.getName() + "@" + field.getType().getSimpleName());
            return null;
        } else if (field.hasAnnotation(OneToOne.class)) {
//            log.info("OneToOne"); //TODO find by entity
//            throw new ComparatorNotFoundException("Not found comparator for OneToOne field "
//                    + field.getName() + "@" + field.getType().getSimpleName());
            return null;
        } else if (field.hasAnnotation(Lob.class)) {
//            log.info("Lob"); //TODO find by entity
//            throw new ComparatorNotFoundException("Not found comparator for Lob field "
//                    + field.getName() + "@" + field.getType().getSimpleName());
            return null;
        } else if (field.hasAnnotation(EmbeddedId.class)) {
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
            log.info("######## " + field.getType() + " " + field.getName());
        }
        return abstractComparator.getExpressions();
    }
}