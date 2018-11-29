package network.giantpay.dto;

public enum Quarter {
    Q1("Q1"),
    Q2("Q2"),
    Q3("Q3"),
    Q4("Q4");

    private final String name;

    Quarter(final String name) {
        this.name = name;
    }

    public String value() {
        return this.name;
    }

    public static Quarter fromValue(final String value) {
        return valueOf(value);
    }
}
