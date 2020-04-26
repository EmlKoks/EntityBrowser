package emlkoks.entitybrowser.mocked.entity;

public enum ExampleEnum {
    VALUE_1("VALUE_1", 1),
    VALUE_2("VALUE_2", 2),
    VALUE_3("VALUE_3", 3);

    private String stringProperty;
    private Integer integerProperty;

    ExampleEnum(String stringProperty, Integer integerProperty) {
        this.stringProperty = stringProperty;
        this.integerProperty = integerProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public Integer getIntegerProperty() {
        return integerProperty;
    }
}
