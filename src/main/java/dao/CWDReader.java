package dao;

import domain.structs.Chart;
import domain.structs.NumberRow;
import java.util.Scanner;
import java.io.File;

/**
 *
 * @author eemeli
 */
public class CWDReader {
    File file;
    
    public CWDReader(String fileName) {
        this.file = new File(fileName);
    }
    
    public Chart read() {
        NumberRow[] leftNumbers;
        NumberRow[] topNumbers;
        try (Scanner fileReader = new Scanner(this.file)) {
            leftNumbers = new NumberRow[Integer.valueOf(fileReader.nextLine())];
            topNumbers = new NumberRow[Integer.valueOf(fileReader.nextLine())];
            for (int i = 0; i < leftNumbers.length; i++) {
                leftNumbers[i] = this.rowToNumberRow(fileReader.nextLine());
            }
            fileReader.nextLine();
            for (int i = 0; i < topNumbers.length; i++) {
                topNumbers[i] = this.rowToNumberRow(fileReader.nextLine());
            }
            return new Chart(topNumbers, leftNumbers);
        } catch (Exception e) {
            System.out.println("File reading error: " + e.getMessage());
        }
        return null;
    }
    
    private NumberRow rowToNumberRow(String row) {
        if (row.isEmpty()) {
            return new NumberRow();
        }
        String[] parts = row.split(" ");
        int[] numbers = new int[parts.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.valueOf(parts[i]);
        }
        return new NumberRow(numbers);
    }
}
