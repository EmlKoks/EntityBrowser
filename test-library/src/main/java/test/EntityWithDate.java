package test;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class EntityWithDate extends IdEntity{
    private Date testDate;
}
