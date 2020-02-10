package phonebook;

public class HashTable {
    private int size;
    private TableEntry[] table;

    public HashTable(int size) {
        this.size = size;
        this.table = new TableEntry[size];
    }

    public int hashFunc(long key) {
        return (int) (key % size);
    }

    public int find(long key, String name) {
        int hash = hashFunc(key);
        if (table[hash] == null)
            return -1;
        if (table[hash].getKey() == key && name.equalsIgnoreCase(table[hash].getName()))
            return hash;
        hash++;
        while (table[hash] != null) {
            if (table[hash].getKey() == key && name.equalsIgnoreCase(table[hash].getName()))
                return hash;
            hash++;
        }
        return -1;
    }

    public TableEntry getEntry(int hash) {
        return table[hash];
    }

    public void displayTable() {
        for (int i = 0; i < size; i++) {
            if (table[i] != null) {
                System.out.println(table[i].getValue());
            } else {
                System.out.println("***");
            }
        }
    }

    private int insertNextNull(int hash, TableEntry entry) {
        while (hash < size && table[hash] != null) {
            ++hash;
        }
        if (hash < size)
            table[hash] = entry;
        else
            hash = 0;
        return (hash);
    }

    public void insert(TableEntry entry) {
        int hash = hashFunc(entry.getKey());
        if (table[hash] == null) {
            table[hash] = entry;
        } else {
            if (insertNextNull(hash, entry) == 0) {
                insertNextNull(0, entry);
            }
        }
    }
}
