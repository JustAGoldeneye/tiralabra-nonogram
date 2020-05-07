package domain.structs;

/**
 * A data structure for row square numbers.
 * @author eemeli
 */
public class NumberRow {
    private final int[] numbers;
    
    /**
     * Creates a nonogram number row with given numbers.
     * @param numbers the numbers to create the number row with
     */
    public NumberRow(int[] numbers) {
        this.numbers = numbers;
    }
    
    /**
     * Creates an empty nonogram number row.
     */
    public NumberRow() {
        this.numbers = new int[0];
    }
    
    /**
     * Looks the number in the given position.
     * @param position  position to look a number from
     * @return          number in the given position
     */
    public int lookNumber(int position) {
        return this.numbers[position];
    }
    
    /**
     * Changes the number in the given position to the given number.
     * @param position  position to look a number from
     * @param number    replaces the number in the given position
     */
    /*
    public void changeNumber(int position, int number) {
        this.numbers[position] = number;
    }
    */
    
    public int length() {
        return this.numbers.length;
    }
}
