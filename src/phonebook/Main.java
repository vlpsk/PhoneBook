package phonebook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class Main {

    private static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    private static void gettingName(StringBuilder recordName,
                                             String[] splitted,
                                             int column) {
        for (int i = column; i < splitted.length; i++) {
            if (i > 1) {
                recordName.append(" ");
            }
            recordName.append(splitted[i]);
        }
    }

    public static String getNameHash(String record, int column)
    {
        String[] splitted = record.split(" ");
        if (splitted.length == 2) {
            splitted = record.split("-");
        }
        StringBuilder recordName = new StringBuilder();
        gettingName(recordName, splitted, column);
        while (recordName.length() == 0) {
            gettingName(recordName, splitted, --column);
        }
        if (recordName.toString().contains("\r"))
            return recordName.substring(0, recordName.length() - 1);
        else
            return (recordName.toString());
    }

    public static String getName(String record, int column)
    {
        String[] splitted = record.split(" ");
        StringBuilder recordName = new StringBuilder();
        gettingName(recordName, splitted, column);
        if (recordName.length() == 0) {
            gettingName(recordName, splitted, column - 1);
        }
        if (recordName.toString().contains("\r"))
            return recordName.substring(0, recordName.length() - 1);
        else
            return (recordName.toString());
    }

    private static int linear_search(String[] records, String name) {
        for (String record : records) {
            String recordName = getName(record, 1);
            if (recordName.equals(name)) {
                return 1;
            }
        }
        return 0;
    }

    private static long bubbleSort(String[] records, long linearTime) {
        int length = records.length;
        long startTime = System.currentTimeMillis();
        for (int j = 0; j < length - 1; j++) {
            for (int i = 0; i < length - 1; i++) {
                String nameOne = getName(records[i], 1);
                String nameTwo = getName(records[i + 1], 1);
                if (nameOne.compareTo(nameTwo) > 0) {
                    String temp = records[i];
                    records[i] = records[i + 1];
                    records[i + 1] = temp;
                }
            }
            long currTime = System.currentTimeMillis();
            if (currTime - startTime > (linearTime * 10)) {
                return currTime - startTime;
            }
        }
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private static int jumpSearch(String[] records, String name)
    {
        int found = 0;
        int step = (int) Math.floor(Math.sqrt(records.length));
        int index = 0;
        while (index < records.length) {
            if (name.equals(getName(records[index], 1))) {
                found++;
                break;
            } else if (name.compareTo(getName(records[index], 1)) > 0) {
                index += step;
            } else {
                for (int i = 0; i < step; i++) {
                    if (index == 0)
                        continue;
                    if (name.equals(getName(records[index - i], 1))) {
                        found++;
                        break;
                    }
                }
                break;
            }
        }
        if (records.length % step != 0)
        {
            while (index < records.length) {
                if (name.equals(getName(records[index++], 1))) {
                    found++;
                    break;
                }
            }
        }
        return (found);
    }

    private static String getTime(long milisec) {
        long sec = milisec / 1000;
        long msec = milisec - (sec * 1000);
        long min = sec / 60;
        sec = sec - min * 60;
        return String.format("%d min. %d sec. %d ms.", min, sec, msec);
    }

    public static long linearSearchStage(String[] records, String[] names) {
        int found = 0;
        int total = 0;
        long startTime = System.currentTimeMillis();
        for (String name : names) {
            found += linear_search(records, name);
            total++;
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.print("Found " + found + " / " + total + " entries. ");
        System.out.println("Time taken: " + getTime(totalTime));
        return totalTime;
    }

    public static void JumpBubbleStage(String[] records, String[] names, long linearTime) {
        long beforeSortTime = System.currentTimeMillis();
        int found = 0;
        int total = 0;
        long bubbleTime = bubbleSort(records, linearTime);
        if ( bubbleTime <= linearTime * 10) {
            long startTime = System.currentTimeMillis();
            for (String name : names) {
                found += jumpSearch(records, name);
                total++;
            }
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - beforeSortTime;
            System.out.print("Found " + found + " / " + total + " entries. ");
            System.out.println("Time taken: " + getTime(totalTime));
            System.out.println("Sorting time: " + getTime(startTime - beforeSortTime));
            System.out.println("Searching time: " + getTime(endTime - startTime));
        }
        else {
            long startTime = System.currentTimeMillis();
            for (String name : names) {
                found += linear_search(records, name);
                total++;
            }
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime + bubbleTime;
            System.out.print("Found " + found + " / " + total + " entries. ");
            System.out.println("Time taken: " + getTime(totalTime));
            System.out.print("Sorting time: " + getTime(bubbleTime));
            System.out.println(" - STOPPED, moved to linear search");
            System.out.println("Searching time: " + getTime(endTime - startTime));
        }
    }

    public static void swap(String[] array, int i, int partitionIndex) {
        String temp = array[i];
        array[i] = array[partitionIndex];
        array[partitionIndex] = temp;
    }

    public static int partition(String[] array, int left, int right)
    {
        String pivot = getName(array[right], 1);
        int partitionIndex = left;
        for (int i = left; i < right; i++) {
            if (getName(array[i], 1).compareTo(pivot) <= 0) {
                swap(array, i, partitionIndex);
                partitionIndex++;
            }
        }
        swap(array, partitionIndex, right);
        return (partitionIndex);
    }

    public static void quickSort(String[] records, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(records, left, right);
            quickSort(records, left, pivotIndex - 1);
            quickSort(records, pivotIndex + 1, right);
        }
    }

    public static int binarySearch(String[] records, String name, int left, int right) {
        if (left > right) {
            return 0;
        }
        int mid = left + (right - left) / 2;
        if (name.equals(getName(records[mid], 1))) {
            return 1;
        } else if (name.compareTo(getName(records[mid], 1)) < 0) {
            return binarySearch(records, name, left, mid - 1);
        } else {
            return binarySearch(records, name, mid + 1, right);
        }
    }

    public static void QuickBinaryStage(String directory, String[] names) {
        long beforeSort = System.currentTimeMillis();
        String[] records = directory.split("\r\n");
        quickSort(records, 0, records.length - 1);
        int total = 0;
        int found = 0;
        long startTime = System.currentTimeMillis();
        for (String name : names) {
            found += binarySearch(records, name, 0, records.length - 1);
            total++;
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - beforeSort;
        long sortTime = startTime - beforeSort;
        System.out.print("Found " + found + " / " + total + " entries. ");
        System.out.println("Time taken: " + getTime(totalTime));
        System.out.println("Sorting time: " + getTime(sortTime));
        System.out.println("Searching time: " + getTime(endTime - startTime));
    }

    private static long myPower(int num, int power) {
        long res = (long) num;
        while (power > 1) {
            res *= num;
            power--;
        }
        if (res < 0)
            res *= -1;
        return (res);
    }

    private static TableEntry generateKey(String str, int flag) {
        String name;

        if (flag == 1){
            name = Main.getNameHash(str, 2).toLowerCase();
        }
        else {
            name = getName(str, 1).toLowerCase();
        }
        int length = name.length();
        long key = 1;
        StringBuilder keyName = new StringBuilder("");
        for (int i = 0; i < length; i++) {
            if (name.charAt(i) < 97 || name.charAt(i) > 122)
                continue;
            keyName.append(name.charAt(i));
        }
        length = keyName.length();
        int i = 0;
        while (length != 0) {
            key = key + (keyName.charAt(i) - 96) *  myPower(13, length);
            i++;
            length--;
        }
        if (key < 0) {
            System.out.println(key);
        }
        TableEntry entry = new TableEntry(key, str, getName(str, 1));
        return (entry);
    }

    private static String[] hashTableStage(String[] records, String[] names) {
        HashTable hashTable = new HashTable(records.length * 2);
        long startTime = System.currentTimeMillis();
        for (String record : records) {
            TableEntry entry = generateKey(record, 1);
            hashTable.insert(entry);
        }
        long midTime = System.currentTimeMillis();
        String[] toReturn = new String[names.length];
        int total = 0;
        int found = 0;
        for (String name : names) {
            long key = generateKey(name, 0).getKey();
            int id = hashTable.find(key, name);
            if (id != -1) {
                toReturn[found] = hashTable.getEntry(id).getValue();
                found++;
            }
            total++;
        }
        long endTime = System.currentTimeMillis();
        System.out.print("Found " + found + " / " + total + " entries. ");
        System.out.println("Time taken: " + getTime(endTime - startTime));
        System.out.println("Creating time: " + getTime(midTime - startTime));
        System.out.println("Searching Time: " + getTime(endTime - midTime));
        return toReturn;
    }

    public static void main(String[] args) {
        try {
            String directory = readFileAsString("./directory.txt");
            String[] records = directory.split("\r\n");
            String find = readFileAsString("./find.txt");
            String[] names = find.split("\r\n");
            System.out.println("Start searching (linear search)...");
            long linearSearchTime = linearSearchStage(records, names);
            /*System.out.println();
            System.out.println("Start searching (bubble sort + jump search)...");
            JumpBubbleStage(records, names, linearSearchTime);*/
            System.out.println();
            System.out.println("Start searching (quick sort + binary search)...");
            QuickBinaryStage(directory, names);
            System.out.println();
            System.out.println("Start searching (hash table)...");
            String[] toWrite = hashTableStage(records, names);
            File output = new File("./foundNumbers.txt");
            FileWriter writer = new FileWriter(output);
            for (String line : toWrite) {
                writer.write(line);
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
