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
    
    public int length() {
        return this.numbers.length;
    }
    
    /**
     * Returns NumberRow as String. Numbers are separated by spaces.
     * @return NumberRow as String
     */
    @Override
    public String toString() {
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < this.length(); i++) {
            current.append(" ").append(this.lookNumber(i));
        }
        return current.toString();
    }
    
    
}
