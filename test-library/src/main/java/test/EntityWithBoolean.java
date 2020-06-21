package test;

import javax.persistence.Entity;

@Entity
public class EntityWithBoolean extends IdEntity {
    private Boolean testBoolean;

    public EntityWithBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }
}
