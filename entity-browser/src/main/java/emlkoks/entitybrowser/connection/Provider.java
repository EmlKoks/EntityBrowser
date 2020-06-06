package emlkoks.entitybrowser.connection;

public enum Provider {
    Hibernate("Hibernate"),
    EclipseLink("EclipseLink");

    private String unitName;

    Provider(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitName() {
        return unitName;
    }

    public static final Provider DEFAULT = Hibernate;
}