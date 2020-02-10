package phonebook;

public class TableEntry {
    private final Long key;
    private final String value;
    private final String name;

    public TableEntry(Long key, String value, String name) {
        this.key = key;
        this.value = value;
        this.name = name;
    }

    public Long getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
