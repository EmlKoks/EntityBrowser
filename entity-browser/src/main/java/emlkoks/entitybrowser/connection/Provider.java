package emlkoks.entitybrowser.connection;

public enum Provider {
    Hibernate,
    EclipseLink;

    public static final Provider DEFAULT = Hibernate;
}