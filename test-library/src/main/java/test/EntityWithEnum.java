package test;

import javax.persistence.Entity;

@Entity
public class EntityWithEnum extends IdEntity {
    private TestEnum testEnum;
}
