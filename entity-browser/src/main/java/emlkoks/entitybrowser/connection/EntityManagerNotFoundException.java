package emlkoks.entitybrowser.connection;

public class EntityManagerNotFoundException extends RuntimeException {

    public EntityManagerNotFoundException(Provider provider) {
        super("Not found provider " + provider);
    }
}
