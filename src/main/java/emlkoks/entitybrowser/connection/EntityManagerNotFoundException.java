package emlkoks.entitybrowser.connection;

public class EntityManagerNotFoundException extends RuntimeException {
    private Provider provider;

    public EntityManagerNotFoundException(Provider provider) {
        this.provider = provider;
    }
}
