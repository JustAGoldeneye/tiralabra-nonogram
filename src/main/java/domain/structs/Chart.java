package domain.structs;

/**
 * A data structure for nonogram charts.
 * @author eemeli
 */
public class Chart {
    private SquareStatus[][] table;
    private NumberRow[] leftNumbers;
    private NumberRow[] topNumbers;
    
    public Chart(NumberRow[] leftNumbers, NumberRow[] topNumbers) {
        this.leftNumbers = leftNumbers;
        this.topNumbers = topNumbers;
        this.table = new SquareStatus[this.leftNumbers.length][this.topNumbers.length];
    }
    
    public Chart(int width, int height) {
        this.table = new SquareStatus[height][width];
        populateTableWithEmpties();
        this.leftNumbers = new NumberRow[height];
        populateNumberRowsWithZeros(this.leftNumbers);
        this.topNumbers = new NumberRow[width];
        populateNumberRowsWithZeros(this.topNumbers);
    }
    
    private void populateTableWithEmpties() {
        for (int i = 0; i < this.table.length; i++) {
            for (int j = 0; j < this.table[i].length; j++) {
                this.table[i][j] = SquareStatus.EMPTY;
            }
        }
    }
    
    private void populateNumberRowsWithZeros(NumberRow[] numberRows) {
        for (int i = 0; i < numberRows.length; i++) {
            numberRows[i] = new NumberRow();
        }
    }
    
    // TODO
    
    /**
     * Prints the chart. Used for debugging
     *
     */
    public void PrintChart() {
        System.out.println("Left:");
        printNumberRows(this.leftNumbers);
        System.out.println("");
        
        System.out.println("Top:");
        printNumberRows(this.topNumbers);
        System.out.println("");
        
        System.out.println("Table:");
        printTable();
    }
    
    // Used for debugging
    private void printNumberRows(NumberRow[] numberRows) {
        for (NumberRow numberRow : numberRows) {
            if (numberRow == null) {
                System.out.println("N");
            } else {
                for (int i = 0; i < numberRow.length(); i++) {
                    System.out.print(numberRow.lookNumber(i) + " ");
                }
                System.out.println("");
            }
        }
        System.out.println(numberRows.length + " rows");
    }
    
    // Used for debugging
    private void printTable() {
        for (int i = 0; i < this.table.length; i++) {
            for (int j = 0; j < this.table[i].length; j++) {
                SquareStatus result = this.table[i][j];
                if (result == null) {
                    System.out.print("N ");
                } else if (result == SquareStatus.EMPTY) {
                    System.out.print("? ");
                } else if (result == SquareStatus.BLACK) {
                    System.out.print("1 ");
                } else if (result == SquareStatus.CROSS) {
                    System.out.print("0 ");
                }else {
                    System.out.print(result + " ");
                }
            }
            System.out.println("");
        }
        System.out.println(this.table.length + " x " + this.table[0].length);
    }
}
